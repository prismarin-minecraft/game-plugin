package in.prismar.game.hardpoint;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.compass.CompassProvider;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.hardpoint.HardpointProvider;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.game.Game;
import in.prismar.game.arsenal.ArsenalService;
import in.prismar.game.hardpoint.config.HardpointConfigFile;
import in.prismar.game.hardpoint.session.HardpointSession;
import in.prismar.game.hardpoint.session.HardpointSessionPlayer;
import in.prismar.game.hardpoint.session.HardpointSessionState;
import in.prismar.game.hardpoint.task.HardpointTask;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.common.random.UniqueRandomizer;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class HardpointFacade implements HardpointProvider {

    private final HardpointConfigFile configFile;

    private final Game game;

    @Setter
    private boolean open = true;

    private final HardpointSession session;

    @Inject
    private ArsenalService arsenalService;

    @Inject
    private CustomItemRegistry itemRegistry;

    @Inject
    private GameStatsDistributor statsDistributor;

    @Inject
    private final ConfigStore configStore;

    @Inject
    private final CompassProvider compassProvider;


    public HardpointFacade(Game game) {
        this.game = game;
        this.configFile = new HardpointConfigFile(game.getDefaultDirectory());
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.session = new HardpointSession();
        this.compassProvider = PrismarinApi.getProvider(CompassProvider.class);
        Bukkit.getScheduler().runTaskTimer(game, new HardpointTask(this), 5, 5);
    }

    @Override
    public int getTeamSize(String team) {
        return session.getPlayers().get(HardpointTeam.valueOf(team)).size();
    }

    public void updateCompass(Player player) {
        compassProvider.removeAllEntries(player);
        compassProvider.addEntry(player, session.getPoint().getLocation(), "Point", "WHITE");
    }

    public int getCurrentlyPlayingCount() {
        int size = 0;
        for (Map.Entry<HardpointTeam, Map<UUID, HardpointSessionPlayer>> entry : session.getPlayers().entrySet()) {
            for (HardpointSessionPlayer sessionPlayer : entry.getValue().values()) {
                size++;
            }
        }
        return size;
    }

    public void executeForAll(Consumer<HardpointSessionPlayer> consumer) {
        for (Map.Entry<HardpointTeam, Map<UUID, HardpointSessionPlayer>> entry : session.getPlayers().entrySet()) {
            for (HardpointSessionPlayer sessionPlayer : entry.getValue().values()) {
                consumer.accept(sessionPlayer);
            }
        }
    }

    public HardpointTeam handleWin(HardpointSession session) {
        int reach = Integer.valueOf(configStore.getProperty("hardpoint.points"));
        for(HardpointTeam team : HardpointTeam.values()) {
            long points = session.getTeamPoints().get(team);
            if(points >= reach) {
                executeForAll(sessionPlayer -> {
                    sessionPlayer.getPlayer().sendMessage(PrismarinConstants.BORDER);
                    sessionPlayer.getPlayer().sendMessage( " ");
                    sessionPlayer.getPlayer().sendMessage(PrismarinConstants.ARROW_RIGHT + " §7Team " + team.getFancyName() + " §7has won the game.");
                    sessionPlayer.getPlayer().sendMessage( " ");
                    sessionPlayer.getPlayer().sendMessage(PrismarinConstants.BORDER);
                    sessionPlayer.getPlayer().playSound(sessionPlayer.getPlayer().getLocation(), Sound.ITEM_TOTEM_USE, 0.4f, 1);
                });
                for(HardpointTeam all : HardpointTeam.values()) {
                    session.getTeamPoints().put(all, 0L);
                }
                return team;
            }
        }
        return null;
    }

    public void open() {
        this.open = true;
    }

    public void close() {
        for (HardpointTeam team : HardpointTeam.values()) {
            List<HardpointSessionPlayer> players = new ArrayList<>(session.getPlayers().get(team).values());
            for (HardpointSessionPlayer sessionPlayer : players) {
                leave(sessionPlayer.getPlayer());
            }
        }
        this.open = false;
    }

    public void join(HardpointTeam team, Player player) {
        session.getPlayers().get(team).put(player.getUniqueId(), new HardpointSessionPlayer(player));
        respawn(player);

        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        provider.recreateTablist(player);
        provider.recreateSidebar(player);

        updateCompass(player);

        sendMessage(PrismarinConstants.PREFIX + "§b" + player.getName() + " §7joined Team " + team.getFancyName());
    }

    public void respawn(Player player) {
        resetPlayer(player, GameMode.ADVENTURE);
        HardpointTeam team = getTeamByPlayer(player);

        arsenalService.fillAmmo(player);
        arsenalService.giveHotbarLoadout(player);
        player.getInventory().setHelmet(itemRegistry.createItem("HardpointHelmet" + team.getFancyName()));
        player.getInventory().setChestplate(itemRegistry.createItem("HardpointChestplate" + team.getFancyName()));
        player.getInventory().setLeggings(itemRegistry.createItem("HardpointLeggings" + team.getFancyName()));
        player.getInventory().setBoots(itemRegistry.createItem("HardpointBoots" + team.getFancyName()));

        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.65f, 1);
        player.teleport(getRandomSpawn(team));


        player.getInventory().setItem(8, itemRegistry.createItem("HardpointLeave"));
    }

    public void leave(Player player) {
        HardpointTeam team = getTeamByPlayer(player);
        sendMessage(PrismarinConstants.PREFIX + "§3" + player.getName() + " §7left Team " + team.getFancyName());
        HardpointSessionPlayer sessionPlayer = session.getPlayers().get(team).remove(player.getUniqueId());
        resetPlayer(player, GameMode.ADVENTURE);
        player.performCommand("spawn");
        statsDistributor.resetKillstreak(player);

        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        provider.recreateTablist(player);
        provider.recreateSidebar(player);

        player.getInventory().setArmorContents(sessionPlayer.getArmor());
        player.getInventory().setStorageContents(sessionPlayer.getContent());

        compassProvider.removeAllEntries(player);

    }

    private void resetPlayer(Player player, GameMode mode) {
        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(mode);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20);
        player.setFoodLevel(20);
    }

    public boolean hasEnoughSpace(HardpointTeam team) {
        int size = session.getPlayers().get(team).size();
        int opSize = 0;
        for (Map.Entry<HardpointTeam, Map<UUID, HardpointSessionPlayer>> entry : session.getPlayers().entrySet()) {
            if (entry.getKey() != team) {
                opSize = entry.getValue().size();
                break;
            }
        }
        int difference = size - opSize;
        return difference <= configFile.getEntity().getMaxSpaceBetweenTeams();
    }

    public void sendMessage(String message) {
        for (Map.Entry<HardpointTeam, Map<UUID, HardpointSessionPlayer>> entry : session.getPlayers().entrySet()) {
            for (HardpointSessionPlayer player : entry.getValue().values()) {
                player.getPlayer().sendMessage(message);
            }
        }
    }

    @Nullable
    public HardpointSessionPlayer getSessionPlayerByPlayer(Player player) {
        return session.getPlayers().get(getTeamByPlayer(player)).get(player.getUniqueId());
    }

    @Nullable
    public HardpointTeam getTeamByPlayer(Player player) {
        for (Map.Entry<HardpointTeam, Map<UUID, HardpointSessionPlayer>> entry : session.getPlayers().entrySet()) {
            if (entry.getValue().containsKey(player.getUniqueId())) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public boolean isCurrentlyPlaying(Player player) {
        for (Map.Entry<HardpointTeam, Map<UUID, HardpointSessionPlayer>> entry : session.getPlayers().entrySet()) {
            if (entry.getValue().containsKey(player.getUniqueId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long getPointRotation() {
        long difference = session.getPointUntil() - System.currentTimeMillis();
        return difference;
    }

    @Override
    public String getCurrentCapturedTeam() {
        if(session.getPointState() == null) {
            return HardpointSessionState.IDLE.getTitle();
        }
        if(session.getPointState() == HardpointSessionState.CAPTURED) {
            return session.getCapturedTeam().getFancyName();
        }
        return session.getPointState().getTitle();
    }

    @Override
    public long getCurrentPoints(String team) {
        return session.getTeamPoints().get(HardpointTeam.valueOf(team));
    }

    public Location getRandomSpawn(HardpointTeam team) {
        return UniqueRandomizer.getRandom("hardpoint" + team.name(), configFile.getEntity().getSpawns().get(team));
    }
}
