package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.warp.WarpProvider;
import in.prismar.game.Game;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerRespawnListener implements Listener {

    @Inject
    private WarzoneService service;


    @EventHandler(priority = EventPriority.HIGH)
    public void onCall(PlayerRespawnEvent event) {
        if(service.isInWarzone(event.getPlayer())) {
            event.setRespawnLocation(service.getWarzoneLocation());
        }
    }
}
