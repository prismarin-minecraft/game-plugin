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
public class AWPGun extends Gun {

    public AWPGun() {
        super("AWP", GunType.SNIPER, Material.NETHERITE_HOE, "§cAWP");
        setZoom(8);
        setShootParticle(Particle.SPELL_INSTANT);
        setAmmoType(AmmoType.SNIPER);
        setSpread(12);
        setSneakSpread(0);

        setRange(160);

        setLegDamage(17);
        setBodyDamage(20);
        setHeadDamage(30);

        setFireRate(40);

        setMaxAmmo(5);

        setReloadTimeInTicks(70);

        generateDefaultLore();


        clearSounds(GunSoundType.SHOOT);
        registerSound(GunSoundType.SHOOT, Sound.ENTITY_ENDERMAN_HURT, 0.7f, 1);

    }
}
