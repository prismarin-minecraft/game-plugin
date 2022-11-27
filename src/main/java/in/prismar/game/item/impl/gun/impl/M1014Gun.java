package in.prismar.game.item.impl.gun.impl;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class M1014Gun extends Gun {

    public M1014Gun() {
        super("M1014", GunType.SHOTGUN, Material.GOLDEN_SHOVEL, "§aM1014");
        setShootParticle(Particle.FLAME);
            setAmmoType(AmmoType.SHOTGUN);
        setSpread(15);
        setSneakSpread(11);
        setRange(17);

        setLegDamage(6);
        setBodyDamage(8);
        setHeadDamage(10);

        setFireRate(40);

        setMaxAmmo(6);
        setBulletsPerShot(8);
        setReloadTimeInTicks(60);

        generateDefaultLore();

        clearSounds(GunSoundType.SHOOT);
        registerSound(GunSoundType.SHOOT, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0.45f, 0.6f);


    }
}
