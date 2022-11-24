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
        setSpread(18);
        setSneakSpread(14);

        setRange(13);

        setLegDamage(5);
        setBodyDamage(7);
        setHeadDamage(8);

        setFireRate(120);

        setMaxAmmo(12);

        setReloadTimeInTicks(60);

        setBulletsPerShot(8);

        generateDefaultLore();

        clearSounds(GunSoundType.SHOOT);
        registerSound(GunSoundType.SHOOT, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.45f, 0.6f);

    }
}