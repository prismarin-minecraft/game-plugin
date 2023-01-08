package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GrenadeItem extends ThrowableItem {
    public GrenadeItem() {
        super("Grenade", Material.STICK, "§6Grenade");
        setCustomModelData(2);
        allFlags();
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Player player = throwEvent.getPlayer();
        Item item = throwEvent.getItem();
        Game game = throwEvent.getGame();
        new BukkitRunnable() {
            int timer = 35;
            @Override
            public void run() {
                if(timer <= 0) {
                    item.getWorld().playSound(item.getLocation(), "grenade.explosion", 2f, 1);
                    for(Entity near : item.getWorld().getNearbyEntities(item.getLocation(), 7, 7, 7)) {
                        if(near instanceof Player target) {
                            double damage = 30 - target.getLocation().distance(item.getLocation());
                            target.damage(damage, player);
                        }
                    }
                    item.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, item.getLocation(), 2);
                    item.remove();
                    cancel();
                    return;
                }
                if(timer % 2 == 0) {
                    item.getWorld().playSound(item.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 2f);
                }
                item.getWorld().spawnParticle(Particle.SMOKE_NORMAL, item.getLocation(), 0);
                timer--;
            }
        }.runTaskTimer(game, 1, 1);
    }
}
