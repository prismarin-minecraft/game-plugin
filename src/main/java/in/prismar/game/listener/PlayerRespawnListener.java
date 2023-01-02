package in.prismar.game.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.warp.WarpProvider;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerRespawnListener implements Listener {

    private final WarpProvider provider;

    public PlayerRespawnListener() {
        this.provider = PrismarinApi.getProvider(WarpProvider.class);
    }

    @EventHandler
    public void onCall(PlayerRespawnEvent event) {
        Location spawn = provider.getWarp("spawn");
        event.setRespawnLocation(spawn);
    }
}
