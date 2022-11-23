package in.prismar.game.map.listener.entity;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.map.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageListener implements Listener {

    @Inject
    private GameMapFacade facade;

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getEntity() instanceof Player target && event.getDamager() instanceof Player damager) {
             double damage = event.getDamage();
             double nextHealth = target.getHealth() - damage;
             if(nextHealth <= 0) {
                 damager.sendTitle("§4☠", "", 5, 20, 5);
                 target.sendTitle("§4You died", "§7from " + damager.getName(), 5, 20, 5);
                 event.setCancelled(true);
                 facade.respawn(target);
                 damager.setHealth(20);
                 facade.sendMessage(PrismarinConstants.PREFIX + "§e" + damager.getName() + " §7banged §c" + target.getName()+"'s §7mom.");
             }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(facade.isCurrentlyPlaying(player)) {
                if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }

        }
    }

}
