package in.prismar.game.item.impl.gun.impl;

import in.prismar.game.Game;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Railgun extends Gun {

    public Railgun() {
        super("Railgun", GunType.SNIPER, Material.DIAMOND_HOE, "§cRailgun");
        setCustomModelData(17);
        setBulletsPerShot(1);
        setZoom(7);
        setAmmoType(AmmoType.RAIL);
        setSpread(10);
        setSneakSpread(0);
        setRange(175);

        setRecoil(0.35);

        setLegDamage(30);
        setBodyDamage(35);
        setHeadDamage(40);

        setFireRate(60);

        setMaxAmmo(1);

        setReloadTimeInTicks(20 * 3);

        registerSound(GunSoundType.SHOOT, "railgunrifle.shoot", 1, 1);
        registerSound(GunSoundType.RELOAD_IN, "reload.sniper.clipin", 1, 1);
        registerSound(GunSoundType.RELOAD_OUT, "reload.sniper.clipout", 1, 1);

        generateDefaultLore();

        setPreviewImage("https://i.imgur.com/gnSfqPO.png");


        buildStateItems();
    }

    @Override
    public void onImpact(Game game, Player player, Location location) {
        location.getWorld().playSound(location, "grenade.explosion", 1.7f, 1f);
        for(Entity near : location.getWorld().getNearbyEntities(location, 7, 7, 7)) {
            if(near instanceof LivingEntity target) {
                double damage = getBodyDamage() - target.getLocation().distance(location);
                target.damage(damage, player);
            }
        }
        location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 2);
    }
}
