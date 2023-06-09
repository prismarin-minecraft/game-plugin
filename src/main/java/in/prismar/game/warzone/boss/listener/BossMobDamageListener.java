package in.prismar.game.warzone.boss.listener;

import in.prismar.game.warzone.boss.Boss;
import in.prismar.game.warzone.boss.BossService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.LivingEntity;
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
public class BossMobDamageListener implements Listener {

    @Inject
    private BossService service;

    @EventHandler
    public void onCall(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager && event.getEntity() instanceof LivingEntity entity) {
            ActiveMob mob = MythicBukkit.inst().getMobManager().getActiveMob(entity.getUniqueId()).orElse(null);
            if (mob == null) {
                return;
            }
            Boss boss = service.getBossById(mob.getType().getInternalName());
            if (boss != null) {
                service.addDamage(boss, mob, damager, event.getDamage());
            }
        }

    }
}