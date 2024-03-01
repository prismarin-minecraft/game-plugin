package in.prismar.game.airdrop;

import in.prismar.library.common.callback.Callback;
import in.prismar.library.spigot.location.LocationUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.A;

import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class AirDrop {
    private final long spawned;
    private final long timeToLive;
    private final long expiration;
    private final Location location;
    private ArmorStand armorStand;
    private final Inventory inventory;

    @Setter
    private Callback removeCallback;

    public AirDrop(Inventory inventory, Location location, long expirationInSeconds) {
        this.inventory = inventory;
        this.location = location;
        this.spawned = System.currentTimeMillis();
        this.timeToLive = System.currentTimeMillis() + 1000 * expirationInSeconds;
        this.expiration = expirationInSeconds;
        spawnArmorStand();
    }

    protected void spawnArmorStand() {
        ArmorStand stand = location.getWorld().spawn(LocationUtil.getCenterOfBlock(location.getBlock().getLocation()), ArmorStand.class);
        stand.setInvisible(true);
        stand.setRightArmPose(new EulerAngle(0, 0, 0));
        this.armorStand = stand;
        final long ticks = 20*expiration;
        double y = 0.4;
        final Particle.DustOptions options = new Particle.DustOptions(Color.RED, 1);
        location.getChunk().load();
        location.getChunk().setForceLoaded(true);
        Scheduler.runTimerFor(1, 1, ticks, new SchedulerRunnable() {
            @Override
            public void run() {
                if(inventory.isEmpty()){
                    setCurrentTicks(1);
                }
                if (getCurrentTicks() == 1) {
                    remove(stand.getLocation());
                    removeCallback.call();
                    return;
                }
                if (stand.isOnGround()) {
                    if (stand.getEquipment().getHelmet() != null) {
                        stand.getEquipment().setHelmet(null);
                    }
                    Location location = stand.getLocation().clone().add(0, 2.2, 0);
                    for (int i = 0; i < 30; i++) {
                        location = location.add(0, 0.5f, 0);

                        location.getWorld().spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1, options);
                    }
                    return;
                }
                setCurrentTicks(ticks);
                Vector vector = stand.getVelocity();
                if (vector.getY() < 0) {
                    vector.setY(vector.getY() * y);
                    stand.setVelocity(vector);
                }
            }
        });
        ItemStack stack = new ItemStack(Material.CLOCK);
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(1);
        stack.setItemMeta(meta);
        stand.getEquipment().setItemInMainHand(stack);

        ItemStack parachute = new ItemStack(Material.CLOCK);
        ItemMeta parachuteMeta = stack.getItemMeta();
        parachuteMeta.setCustomModelData(2);
        parachute.setItemMeta(parachuteMeta);
        stand.getEquipment().setHelmet(parachute);
    }

    protected void remove(Location location) {
        for(Entity stand : location.getWorld().getNearbyEntities(location, 3, 150, 3, entity -> entity instanceof ArmorStand)) {
            stand.remove();
        }
        location.getChunk().setForceLoaded(false);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiration;
    }

}
