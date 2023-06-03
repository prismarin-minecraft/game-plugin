package in.prismar.game.item.impl.gun.impl;

import in.prismar.api.user.data.SeasonData;
import in.prismar.game.Game;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GrenadeLauncherItem extends Gun {

    private static final double SHOOT_STRENGTH = 2;

    public GrenadeLauncherItem() {
        super("GrenadeLauncher", GunType.SPECIAL, Material.DIAMOND_AXE, "§cGrenade Launcher");
        setCustomModelData(5);
        setAmmoType(AmmoType.GRENADE_LAUNCHER);
        setMaxAmmo(16);
        setHeadDamage(24);
        setBodyDamage(24);
        setLegDamage(24);
        setSmallLore(true);

        setFireRate(200);

        setReloadTimeInTicks(100);

        registerSound(GunSoundType.RELOAD_IN, "reload.lmg.clipin", 1.0f, 1f);
        registerSound(GunSoundType.RELOAD_OUT, "reload.lmg.clipout", 1.5f, 1f);
        registerSound(GunSoundType.SHOOT, "shoot.grenadelauncher", 0.5f, 1);

        generateDefaultLore();
    }

    @Override
    protected void rawShoot(Game game, Player player, GunPlayer gunPlayer, SeasonData seasonData, ItemStack stack) {
        Vector vector = player.getLocation().getDirection().multiply(SHOOT_STRENGTH);
        Item item = player.getWorld().dropItem(player.getEyeLocation(), getAmmoType().getItem().clone());
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(vector);
        player.updateInventory();
        new BukkitRunnable() {

            long ticks = 0;

            @Override
            public void run() {
                if (!player.isOnline() || ticks >= 20 * 60) {
                    if (!item.isDead()) {
                        item.remove();
                    }
                    cancel();
                    return;
                }
                if (item.isOnGround()) {
                    item.remove();
                    item.getWorld().playSound(item.getLocation(), "grenade.explosion", 1.7f, 1f);
                    for (Entity near : item.getWorld().getNearbyEntities(item.getLocation(), 7, 7, 7)) {
                        if (near instanceof LivingEntity target) {
                            double damage = getBodyDamage() - target.getLocation().distance(item.getLocation());
                            target.damage(damage, player);
                        }
                    }
                    item.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, item.getLocation(), 2);
                    cancel();
                    return;
                }
                ticks++;
            }
        }.runTaskTimer(game, 1, 1);
    }
}
