package in.prismar.game.extraction.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    private ExtractionFacade facade;

    @Inject
    private GameStatsDistributor statsDistributor;



    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getEntity() instanceof Player target && event.getDamager() instanceof Player damager) {
            if(facade.isIn(target) && facade.isIn(damager)) {
                double damage = event.getDamage();
                double nextHealth = target.getHealth() - damage;
                if(nextHealth <= 0) {
                    event.setCancelled(true);
                    handleDeath(target, damager);
                }
            }
        }
    }

    private void handleDeath(Player target, Player damager) {
        target.sendTitle("§4You died", "", 5, 20, 5);
        facade.kill(target);

        if(damager != null) {
            int health = (int)damager.getHealth();
            facade.sendMessage(PrismarinConstants.PREFIX + "§c" +target.getName() + " §7got killed by §3" + damager.getName() + " §8(§c"+health+"♥§8)");
        } else {
            facade.sendMessage(PrismarinConstants.PREFIX + "§c" + target.getName() + " §7just died.");
        }
        boolean samePlayer = damager == null ? true : damager.getUniqueId().equals(target.getUniqueId());
        if(!samePlayer) {
            statsDistributor.addKill(damager);
            statsDistributor.addExtractionKill(damager);
        }
        statsDistributor.addDeath(target);
        statsDistributor.addExtractionDeath(target);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(facade.isIn(player)) {
                if(event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                    double damage = 4;
                    double nextHealth = player.getHealth() - damage;
                    if(nextHealth <= 0.0) {
                        player.setFireTicks(1);
                        event.setCancelled(true);
                        handleDeath(player, null);
                    } else {
                        event.setDamage(damage);
                    }
                    return;
                }
                if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setCancelled(true);
                }
            }

        }
    }


}
