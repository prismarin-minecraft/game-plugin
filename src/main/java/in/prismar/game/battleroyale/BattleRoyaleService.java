package in.prismar.game.battleroyale;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.game.Game;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.game.battleroyale.countdown.impl.QueueCountdown;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueJoinEvent;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueLeaveEvent;
import in.prismar.game.battleroyale.model.*;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.text.InteractiveTextBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
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

    public void switchState(BattleRoyaleGame game, BattleRoyaleGame.BattleRoyaleGameState state) {
        game.setState(state);
        switch (state) {
            case QUEUE -> game.setCountdown(new QueueCountdown(this, game));
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
