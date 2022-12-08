package in.prismar.game.extraction;

import in.prismar.api.PrismarinApi;
import in.prismar.api.map.ExtractionProvider;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.Game;
import in.prismar.game.extraction.map.ExtractionMapFile;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class ExtractionFacade implements ExtractionProvider {

    private ExtractionMapFile mapFile;
    private final RegionProvider regionProvider;

    private int currentMapSpawnIndex = -1;

    public ExtractionFacade(Game game) {
        this.mapFile = new ExtractionMapFile(game.getDefaultDirectory());
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);
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

    @Override
    public boolean isInSafeZone(Player player) {
        return regionProvider.isInRegionWithFlag(player.getLocation(), "safezone");
    }
}
