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
public class BallistaGun extends Gun {

    public BallistaGun() {
        super("Ballista", GunType.SNIPER, Material.IRON_HOE, "§cBallista");
        setShootParticle(Particle.SPELL_INSTANT);
        setAmmoType(AmmoType.SNIPER);
        setSpread(10);
        setSneakSpread(0);

        setRange(160);

        setLegDamage(14);
        setBodyDamage(18);
        setHeadDamage(26);

        setFireRate(60);

        setMaxAmmo(7);

        setReloadTimeInTicks(80);

        generateDefaultLore();


        clearSounds(GunSoundType.SHOOT);
        registerSound(GunSoundType.SHOOT, Sound.ENTITY_ENDERMAN_HURT, 0.7f, 1);

    }
}
