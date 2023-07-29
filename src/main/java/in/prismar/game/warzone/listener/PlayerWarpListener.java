package in.prismar.game.warzone.listener;

import in.prismar.api.warp.WarpEvent;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerWarpListener implements Listener {

    @Inject
    private WarzoneService service;

    @EventHandler
    public void onCall(WarpEvent event) {
        if(event.getId().equalsIgnoreCase("warzone") && service.isClosed()) {
            event.setCancelled(true);
            return;
        }
        if(service.isInWarzone(event.getPlayer())) {
            if(service.isInSafeZone(event.getPlayer())) {
                event.setTimer(Long.parseLong(service.getConfigStore().getProperty("warzone.teleport.timer")));
            }
        }

    }
}
