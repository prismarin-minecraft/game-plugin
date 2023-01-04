package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

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

        setOnThrow(throwEvent -> {
            Player player = throwEvent.getPlayer();
            Item item = throwEvent.getItem();
            Game game = throwEvent.getGame();
            new BukkitRunnable() {
                int timer = 35;
                @Override
                public void run() {
                    if(timer <= 0) {
                        item.getWorld().playSound(item.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 2f, 1);
                        for(Entity near : item.getWorld().getNearbyEntities(item.getLocation(), 7, 7, 7)) {
                            if(near instanceof Player target) {
                                double damage = 24 - target.getLocation().distance(item.getLocation());
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
        });
    }


}
