package in.prismar.game.map.listener.entity;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.map.GameMapFacade;
import in.prismar.library.common.math.MathUtil;
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

    private ConfigStore store;

    public EntityDamageListener() {
        this.store = PrismarinApi.getProvider(ConfigStore.class);
    }

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
                 target.sendTitle("§4You died", "", 5, 20, 5);
                 event.setCancelled(true);
                 facade.respawn(target);
                 damager.setHealth(20);
                 facade.sendMessage(PrismarinConstants.PREFIX + getRandomDeathMessage(damager, target));
             }
        }
    }

    public String getRandomDeathMessage(Player killer, Player target) {
        String[] messages = store.getProperty("death.messages").split("/");
        String random = messages[MathUtil.random(messages.length - 1)];
        return random.replace("&", "§").replace("{killer}", killer.getName())
                .replace("{target}", target.getName());
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
