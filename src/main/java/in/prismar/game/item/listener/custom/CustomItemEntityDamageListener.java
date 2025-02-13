package in.prismar.game.item.listener.custom;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.game.item.impl.gun.type.GunDamageType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class CustomItemEntityDamageListener implements Listener {

    @Inject
    private CustomItemRegistry registry;

    @Inject
    private Game game;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            registry.publishEvent(player, event);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCall(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager) {
            registry.publishEvent(damager, event);
        }
        if (event.getEntity() instanceof Player player) {
            List<CustomItemHolder> holders = registry.publishEvent(player, event);
            int headProtection = 0;
            int bodyProtection = 0;
            for (CustomItemHolder holder : holders) {
                if (holder.getHoldingType() != CustomItemHoldingType.ARMOR) {
                    continue;
                }
                if (holder.getItem() instanceof ArmorItem armor) {
                    headProtection += armor.getHeadProtection();
                    bodyProtection += armor.getBodyProtection();
                }
            }
            int reducePercentage = bodyProtection;
            GunPlayer gunPlayer = GunPlayer.of(player.getUniqueId());
            if (gunPlayer.getLastDamageReceived() != null) {
                if (gunPlayer.getLastDamageReceived() == GunDamageType.HEADSHOT) {
                    reducePercentage = headProtection;
                }
            }
            if (reducePercentage > 0) {
                double damage = event.getDamage();
                double reducedDamage = (damage / 100.0) * (double) reducePercentage;
                double finalDamage = MathUtil.clamp(damage - reducedDamage, 0, damage);
                event.setDamage(finalDamage);
            }
        }

    }
}
