package in.prismar.game.warzone;

import in.prismar.api.PrismarinApi;
import in.prismar.api.warp.WarpProvider;
import in.prismar.api.warzone.WarzoneProvider;
import in.prismar.library.meta.anno.Service;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class WarzoneService implements WarzoneProvider {

    private static final String WARP_NAME = "warzone";

    private final WarpProvider warpProvider;

    public WarzoneService() {
        this.warpProvider = PrismarinApi.getProvider(WarpProvider.class);
    }

    public void teleportToWarzone(Player player) {
        warpProvider.teleport(player, WARP_NAME);
    }

    public Location getWarzoneLocation() {
        return warpProvider.getWarp(WARP_NAME);
    }

    @Override
    public boolean isInWarzone(Player player) {
        Location location = warpProvider.getWarp(WARP_NAME);
        if(location != null) {
            return player.getWorld().getName().equals(location.getWorld().getName());
        }
        return false;
    }
}
