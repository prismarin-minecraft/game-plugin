package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageByEntityListener implements Listener {

    @Inject
    private WarzoneService service;

    @EventHandler
    public void onCall(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager) {
            if(service.isInWarzone(player) && service.isInWarzone(damager)) {
                if(service.isOnNewbieProtection(damager)) {
                    event.setCancelled(true);
                    damager.sendMessage(PrismarinConstants.PREFIX + "§cYou can't damage this player while having the newbie protection on. (Disable with /newbie)");
                    return;
                }
                if(service.isOnNewbieProtection(player)) {
                    event.setCancelled(true);
                    damager.sendMessage(PrismarinConstants.PREFIX + "§cYou can't damage this player because of newbie protection");
                    return;
                }
            }

        }
    }
}
