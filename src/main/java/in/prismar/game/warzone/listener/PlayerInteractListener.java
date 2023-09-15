package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractListener implements Listener {

    @Inject
    private WarzoneService service;


    @EventHandler(priority = EventPriority.HIGH)
    public void onCall(PlayerInteractEvent event) {
        if(event.getItem() == null) {
            return;
        }
        if(event.getItem().getType() == Material.FISHING_ROD) {
            Player player = event.getPlayer();
            if(service.isInWarzone(player) && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "fishingrods.bypass")) {
                event.setCancelled(true);
            }
        }

    }
}
