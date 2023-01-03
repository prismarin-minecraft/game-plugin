
package in.prismar.game.ffa.listener.block;

import in.prismar.game.ffa.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class BlockBreakEntityListener implements Listener {

    @Inject
    private GameMapFacade facade;

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof ArmorStand) {
            if(event.getDamager() instanceof Player damager) {
                if(facade.isCurrentlyPlaying(damager)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onBreak(HangingBreakByEntityEvent event) {
        if(event.getRemover() instanceof Player remover) {
            if(facade.isCurrentlyPlaying(remover)) {
                event.setCancelled(true);
            }
        }
    }
}
