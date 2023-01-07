package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AirstrikeItem extends ThrowableItem {
    public AirstrikeItem() {
        super("Airstrike", Material.STICK, "§4§lAirstrike");
        setCustomModelData(1);
        allFlags();
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Game game = throwEvent.getGame();
        Item item = throwEvent.getItem();
        Player player = throwEvent.getPlayer();
        ItemStack stack = new ItemBuilder(Material.CLOCK).setCustomModelData(3).build();
        final double y = 70;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(item.isOnGround()) {
                    cancel();
                    Location start = item.getLocation().clone();
                    item.remove();
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 3f, 1);

                    List<ArmorStand> spawned = new ArrayList<>();
                    final Particle.DustOptions options = new Particle.DustOptions(Color.YELLOW, 1);
                    Scheduler.runTimerFor(10, 10, 20 * 20, new SchedulerRunnable() {
                        @Override
                        public void run() {
                            Location particle = start.clone().add(0, 2.2, 0);
                            for (int i = 0; i < 30; i++) {
                                particle = particle.add(0, 0.5f, 0);
                                particle.getWorld().spawnParticle(Particle.REDSTONE, particle.getX(), particle.getY(), particle.getZ(), 1, options);
                            }
                            if(getCurrentTicks() <= 20) {
                                for(ArmorStand armorStand : spawned) {
                                    armorStand.remove();
                                }
                                spawned.clear();
                                cancel();
                                return;
                            }
                            Iterator<ArmorStand> iterator = spawned.iterator();
                            while (iterator.hasNext()) {
                                ArmorStand armorStand = iterator.next();
                                if(armorStand.isOnGround()) {
                                    armorStand.remove();
                                    iterator.remove();
                                    armorStand.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, armorStand.getLocation(), 1);
                                    armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 3f, 1);
                                    for(Entity near : item.getWorld().getNearbyEntities(armorStand.getLocation(), 7, 7, 7)) {
                                        if(near instanceof Player target) {
                                            double damage = 30 - target.getLocation().distance(armorStand.getLocation());
                                            target.damage(damage, player);
                                        }
                                    }
                                } else {
                                    armorStand.getLocation().getWorld().playSound(armorStand.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.5f, 1.5f);
                                }
                            }
                            if(getCurrentTicks() % 5 == 0) {
                                for (int i = 0; i < 1; i++) {
                                    Location location = start.clone().add(MathUtil.random(-20, 20), y, MathUtil.random(-20, 20));
                                    spawned.add(spawnMissile(stack, location));
                                }

                            }
                        }
                    });

                    return;
                }
                item.getWorld().spawnParticle(Particle.SMOKE_NORMAL, item.getLocation(), 0);
            }
        }.runTaskTimer(game, 1, 1);
    }

    private ArmorStand spawnMissile(ItemStack stack, Location location) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(true);
        armorStand.getEquipment().setHelmet(stack);
        return armorStand;
    }


}
