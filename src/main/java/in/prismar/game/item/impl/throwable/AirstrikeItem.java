package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

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
        player.playSound(player.getLocation(), "airstrike.request", 0.8f, 1);
        ItemStack stack = new ItemBuilder(Material.CLOCK).setCustomModelData(3).build();
        final double y = 70;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(item.isOnGround()) {
                    cancel();
                    Location start = item.getLocation().clone();
                    item.remove();
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
                                    armorStand.getLocation().getWorld()
                                            .playSound(armorStand.getLocation(), "airstrike.impact", 5f, 1);
                                    for(Entity near : item.getWorld().getNearbyEntities(armorStand.getLocation(), 7, 7, 7)) {
                                        if(near instanceof LivingEntity target) {
                                            double damage = 30 - target.getLocation().distance(armorStand.getLocation());
                                            target.damage(damage, player);
                                        }
                                    }
                                }
                            }
                            if(getCurrentTicks() % 2 == 0) {
                                for (int i = 0; i < 4; i++) {
                                    Location location = start.clone().add(MathUtil.random(-30, 30), y, MathUtil.random(-30, 30));
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
