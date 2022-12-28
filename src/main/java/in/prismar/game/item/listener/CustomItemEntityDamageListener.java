package in.prismar.game.item.listener;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.GunPlayer;
import in.prismar.game.item.impl.gun.type.GunDamageType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
    public void onCall(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player) {
            List<CustomItemHolder> holders = registry.publishEvent(player, event);
            for(CustomItemHolder holder : holders) {
                if(holder.getHoldingType() != CustomItemHoldingType.ARMOR) {
                    continue;
                }
                int headProtection = 0;
                int bodyProtection = 0;
                if(holder.getItem() instanceof ArmorItem armor) {
                    headProtection += armor.getHeadProtection();
                    bodyProtection += armor.getBodyProtection();
                }

                int reducePercentage = bodyProtection;
                GunPlayer gunPlayer = GunPlayer.of(player.getUniqueId());
                if(gunPlayer.getLastDamageReceived() != null) {
                    if(gunPlayer.getLastDamageReceived() == GunDamageType.HEADSHOT) {
                        reducePercentage = headProtection;
                    }
                }
                double damage = event.getDamage();
                double reducedDamage = (damage / 100.0) * (double) reducePercentage;
                double finalDamage = MathUtil.clamp(damage - reducedDamage, 0, damage);
                event.setDamage(finalDamage);
            }
        }

    }
}
