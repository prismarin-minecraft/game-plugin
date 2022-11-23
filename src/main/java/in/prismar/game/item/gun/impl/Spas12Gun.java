package in.prismar.game.item.gun.impl;

import in.prismar.game.item.gun.Gun;
import in.prismar.game.item.gun.sound.GunSoundType;
import in.prismar.game.item.gun.type.AmmoType;
import in.prismar.game.item.gun.type.GunType;
import org.bukkit.Material;
import org.bukkit.Sound;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Spas12Gun extends Gun {

    public Spas12Gun() {
        super("Spas-12", GunType.SHOTGUN, Material.IRON_SHOVEL, "§fSpas-12");
        setAmmoType(AmmoType.SHOTGUN);
        setSpread(25);
        setSneakSpread(20);

        setRange(13);

        setLegDamage(2);
        setBodyDamage(4);
        setHeadDamage(6);

        setFireRate(120);

        setMaxAmmo(12);

        setReloadTimeInTicks(60);

        setBulletsPerShot(5);

        generateDefaultLore();

        clearSounds(GunSoundType.SHOOT);
        registerSound(GunSoundType.SHOOT, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.5f, 0.6f);

    }
}
