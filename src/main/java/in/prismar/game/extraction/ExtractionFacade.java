package in.prismar.game.extraction;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.map.ExtractionProvider;
import in.prismar.api.region.RegionProvider;
import in.prismar.api.warp.WarpProvider;
import in.prismar.game.Game;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.extraction.corpse.Corpse;
import in.prismar.game.extraction.map.ExtractionMapFile;
import in.prismar.game.extraction.task.ExtractionChecker;
import in.prismar.game.extraction.task.ExtractionCorpseDespawner;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class ExtractionFacade implements ExtractionProvider {

    @Inject
    private AirDropRegistry airDropRegistry;

    private ExtractionMapFile mapFile;
    private final Game game;
    private final RegionProvider regionProvider;

    private ExtractionChecker checker;

    private List<Corpse> corpses;

    private ConfigStore configStore;

    @Setter
    private boolean running;
    private int currentMapSpawnIndex = 0;

    public ExtractionFacade(Game game) {
        this.game = game;
        this.mapFile = new ExtractionMapFile(game.getDefaultDirectory());
        this.corpses = new ArrayList<>();
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, checker = new ExtractionChecker(this), 20, 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new ExtractionCorpseDespawner(this), 20, 20);
    }


    public void open() {
        setRunning(true);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.PREFIX + "§c§lEXTRACTION §7is now open!");
        Bukkit.broadcastMessage(" ");
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.7f, 1);
        }
    }

    public void close() {
        setRunning(false);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.PREFIX + "§c§lEXTRACTION §7is now closed!");
        Bukkit.broadcastMessage(" ");
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 0.7f, 1);
        }
        World world = getExtractionWorld();
        if(world != null) {
            WarpProvider provider = PrismarinApi.getProvider(WarpProvider.class);
            for(Player player : world.getPlayers()) {
                provider.teleportToSpawn(player);
            }
        }
    }

    public void join(Player player) {
        Location random = getMapSpawnLocation();
        player.teleport(random);

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6f, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 0.5f, 1);

        for(Corpse corpse : corpses) {
            corpse.getNpc().forceUpdate(player);
        }
    }

    public void leave(Player player) {
        WarpProvider provider = PrismarinApi.getProvider(WarpProvider.class);
        provider.teleportToSpawn(player);
        player.setHealth(20);
    }

    public void kill(Player player) {
        //TODO: Spawn dead body
        if(!player.getInventory().isEmpty()) {
            long seconds = Long.valueOf(configStore.getProperty("extraction.corpse.despawn"));
            this.corpses.add(new Corpse(game, player, System.currentTimeMillis() + 1000 * seconds));
        }
        player.getInventory().clear();
        leave(player);
    }

    public void sendMessage(String message) {
        World world = getExtractionWorld();
        if(world != null) {
            for(Player player : world.getPlayers()) {
                player.sendMessage(message);
            }
        }
    }

    public Location getMapSpawnLocation() {
        if(currentMapSpawnIndex >= mapFile.getEntity().getSpawns().size()) {
            currentMapSpawnIndex = 0;
        }
        Location location = mapFile.getEntity().getSpawns().get(currentMapSpawnIndex);
        currentMapSpawnIndex++;
        return location;
    }

    @Override
    public boolean isIn(Player player) {
       if(!getMapFile().getEntity().getSpawns().isEmpty()) {
           final Location location = getMapFile().getEntity().getSpawns().get(0);
           return player.getWorld().getName().equals(location.getWorld().getName());
       }
       return false;
    }

    public World getExtractionWorld() {
        if(getMapFile().getEntity().getSpawns().isEmpty()) {
           return null;
        }
        final Location location = getMapFile().getEntity().getSpawns().get(0);
        return location.getWorld();
    }

    @Override
    public boolean isInSafeZone(Player player) {
        return regionProvider.isInRegionWithFlag(player.getLocation(), "safezone");
    }
}
