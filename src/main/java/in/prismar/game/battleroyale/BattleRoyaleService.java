package in.prismar.game.battleroyale;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.meta.Provider;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.game.battleroyale.countdown.impl.InGameCountdown;
import in.prismar.game.battleroyale.countdown.impl.QueueCountdown;
import in.prismar.game.battleroyale.countdown.impl.WarmUpCountdown;
import in.prismar.game.battleroyale.event.BattleRoyaleLeaveEvent;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueJoinEvent;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueLeaveEvent;
import in.prismar.game.battleroyale.model.*;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.text.InteractiveTextBuilder;
import io.lumine.mythic.core.utils.RandomUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Service
@Getter
public class BattleRoyaleService {

    @Inject
    private BattleRoyaleArenaService arenaService;

    @Inject
    private BattleRoyaleRegistry registry;

    @Inject
    private Game game;

    @Inject
    private final ScoreboardProvider scoreboardProvider;


    public BattleRoyaleService() {
        this.scoreboardProvider = PrismarinApi.getProvider(ScoreboardProvider.class);
        this.scoreboardProvider.addSidebarProvisioner(new BattleRoyaleSidebarProvisioner(this));
    }


    public BattleRoyaleGame create(String props, BattleRoyaleArena arena) throws Exception {
        BattleRoyaleProperties properties = new BattleRoyaleProperties();
        if(!props.isBlank()) {
            String[] args = props.split(" ");
            for(String prop : args) {
                String[] split = prop.split("=");
                String fieldName = split[0];
                String value = split[1];
                for(Field field : properties.getClass().getDeclaredFields()) {
                    if(!field.canAccess(properties)) {
                        field.setAccessible(true);
                    }
                    if(field.getName().equalsIgnoreCase(fieldName)) {
                        if(field.getType() == int.class) {
                            field.setInt(properties, Integer.parseInt(value));
                        }
                        break;
                    }
                }
            }
        }

        BattleRoyaleGame game = registry.create(properties, arena);
        switchState(game, BattleRoyaleGame.BattleRoyaleGameState.QUEUE);
        return game;
    }

    public void shuffleTeams(BattleRoyaleGame game) {
        List<BattleRoyaleQueueEntry> entries = new CopyOnWriteArrayList<>(game.getQueue());
        for(BattleRoyaleQueueEntry entry : entries) {
            if(entry.getPlayers().size() >= game.getProperties().getTeamSize()) {
                BattleRoyaleTeam team = new BattleRoyaleTeam();
                for(Player player : entry.getPlayers().values()) {
                    team.registerParticipant(player);
                }
                game.getTeams().add(team);
                entries.remove(entry);
                continue;
            }
            boolean found = false;
            for(BattleRoyaleQueueEntry otherEntry : entries) {
                int next = entry.getPlayers().size() + otherEntry.getPlayers().size();
                if(next <= game.getProperties().getTeamSize()) {
                    found = true;
                    BattleRoyaleTeam team = new BattleRoyaleTeam();
                    for(Player player : entry.getPlayers().values()) {
                        team.registerParticipant(player);
                    }
                    for(Player player : otherEntry.getPlayers().values()) {
                        team.registerParticipant(player);
                    }
                    game.getTeams().add(team);
                    entries.remove(entry);
                    entries.remove(otherEntry);
                    break;
                }
            }
            if(!found) {
                BattleRoyaleTeam team = new BattleRoyaleTeam();
                for(Player player : entry.getPlayers().values()) {
                    team.registerParticipant(player);
                }
                game.getTeams().add(team);
                entries.remove(entry);
            }
        }
        game.getQueue().clear();
    }

    public void removeFromGame(Player player, boolean force) {
        BattleRoyaleGame game = getRegistry().getByPlayer(player);
        if(game == null) {
            return;
        }
        BattleRoyaleTeam team = getRegistry().getTeamByPlayer(game, player);
        BattleRoyaleParticipant participant = team.getParticipants().remove(player.getUniqueId());
        if(team.getParticipants().isEmpty()) {
            game.getTeams().remove(team);
        }

        Bukkit.getPluginManager().callEvent(new BattleRoyaleLeaveEvent(game, team, participant, force));
    }

    public void randomTeleport(BattleRoyaleGame game, BattleRoyaleTeam team) {
        double size = ((double) game.getArena().getSize() / 2) - 200;
        Location location = game.getArena().getCenter().clone().add(MathUtil.randomDouble(-size, size), 0, MathUtil.randomDouble(-size, size));
        location.setY(game.getArena().getSpawnYLevel());

        double radius = 7;
        for(BattleRoyaleParticipant participant : team.getParticipants().values()) {
            Location spawn = location.clone().add(MathUtil.randomDouble(-radius, radius), 0, MathUtil.randomDouble(-radius, radius));
            participant.getPlayer().teleport(spawn);
        }
    }

    public void switchState(BattleRoyaleGame game, BattleRoyaleGame.BattleRoyaleGameState state) {
        game.setState(state);
        switch (state) {
            case QUEUE -> game.setCountdown(new QueueCountdown(this, game));
            case WARM_UP -> game.setCountdown(new WarmUpCountdown(this, game));
            case IN_GAME -> game.setCountdown(new InGameCountdown(this, game));
        }
        game.getCountdown().start();
    }

    public void announceBattleRoyale(BattleRoyaleGame game) {
        final String arrow = PrismarinConstants.ARROW_RIGHT.concat(" ");
        InteractiveTextBuilder builder = new InteractiveTextBuilder(arrow);
        builder.addText("§8[§a§lJoin queue§8]", "/broyale queue join " + game.getId(), "§aClick §7to join the queue");
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(PrismarinConstants.BORDER);
            player.sendMessage(" ");
            player.sendMessage(arrow + "§a§lBattleRoyale Event");
            player.sendMessage(arrow + "§7starts in §2" + TimeUtil.showInMinutesSeconds(game.getCountdown().getCurrentSeconds()));
            player.sendMessage( " ");
            builder.send(player);
            player.sendMessage(" ");
            player.sendMessage(PrismarinConstants.BORDER);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.5f, 1f);
        }
    }

    public BattleRoyaleQueueEntry addToQueue(BattleRoyaleGame game, Player player) {
        BattleRoyaleQueueEntry entry = new BattleRoyaleQueueEntry();
        entry.getPlayers().put(player.getUniqueId(), player);
        Bukkit.getPluginManager().callEvent(new BattleRoyaleQueueJoinEvent(game, entry, player));
        game.getQueue().add(entry);
        return entry;
    }

    public void addToSpecificQueue(BattleRoyaleGame game, BattleRoyaleQueueEntry entry, Player player) {
        entry.getPlayers().put(player.getUniqueId(), player);
        Bukkit.getPluginManager().callEvent(new BattleRoyaleQueueJoinEvent(game, entry, player));
    }

    public int getQueuePlayerCount(BattleRoyaleGame game) {
        int count = 0;
        for(BattleRoyaleQueueEntry entry : game.getQueue()) {
            count += entry.getPlayers().size();
        }
        return count;
    }

    public void sendQueueMessage(BattleRoyaleQueueEntry entry, String message) {
        for(Player player : entry.getPlayers().values()) {
            player.sendMessage(message);
        }
    }

    public BattleRoyaleQueueEntry removeFromQueue(Player player) {
        BattleRoyaleGame game = getRegistry().getByPlayer(player);
        if(game != null) {
            BattleRoyaleQueueEntry entry = registry.getQueueEntryByPlayer(game, player);
            if(entry != null) {
                Bukkit.getPluginManager().callEvent(new BattleRoyaleQueueLeaveEvent(game, entry, player));
                entry.getPlayers().remove(player.getUniqueId());
                if(entry.getPlayers().isEmpty()) {
                    game.getQueue().remove(entry);
                    return entry;
                }
            }
        }
        return null;
    }

    public void executeForAll(BattleRoyaleGame game, Consumer<Player> consumer) {
        for(BattleRoyaleQueueEntry entry : game.getQueue()) {
            for(Player player : entry.getPlayers().values()) {
                consumer.accept(player);
            }
        }
        for(BattleRoyaleTeam team : game.getTeams()) {
            for(BattleRoyaleParticipant participant : team.getParticipants().values()) {
                consumer.accept(participant.getPlayer());
            }
        }
    }

    public void sendMessage(BattleRoyaleGame game, String message) {
        executeForAll(game, player -> player.sendMessage(message));
    }
}
