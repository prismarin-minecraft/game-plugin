package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ImpactGrenadeItem extends LethalItem {

    private static final double DAMAGE = 24;

    public ImpactGrenadeItem() {
        super("ImpactGrenade", Material.STICK, "§cImpact Grenade");
        setCustomModelData(6);
        allFlags();
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Item item = throwEvent.getItem();
        Game game = throwEvent.getGame();

        new BukkitRunnable() {

            long ticks = 0;
            @Override
            public void run() {
                if (!throwEvent.getPlayer().isOnline() || ticks >= 20 * 60) {
                    if (!item.isDead()) {
                        item.remove();
                    }
                    cancel();
                    return;
                }
                if (item.isOnGround()) {
                    item.remove();
                    cancel();
                    if(callExplodeEvent(throwEvent, item.getLocation())) {
                        return;
                    }
                    item.getWorld().playSound(item.getLocation(), "grenade.explosion", 1.7f, 1f);
                    for(Entity near : item.getWorld().getNearbyEntities(item.getLocation(), 7, 7, 7)) {
                        if(near instanceof LivingEntity target) {
                            double damage = DAMAGE - target.getLocation().distance(item.getLocation());
                            target.damage(damage, throwEvent.getPlayer());
                        }
                    }
                    item.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, item.getLocation(), 2);
                    return;
                }
                if (ticks % 2 == 0 && !item.isOnGround()) {
                    item.getWorld().playSound(item.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 2f);
                }
                ticks++;
            }
        }.runTaskTimer(game, 1, 1);
    }
}
