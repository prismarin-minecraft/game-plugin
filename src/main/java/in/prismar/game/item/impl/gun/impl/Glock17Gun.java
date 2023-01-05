package in.prismar.game.item.impl.gun.impl;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Glock17Gun extends Gun {

    public Glock17Gun() {
        super("Glock17", GunType.PISTOL, Material.WOODEN_HOE, "§7Glock 17");
        setCustomModelData(4);
        setZoom(2);
        setAmmoType(AmmoType.PISTOL);
        setSpread(2);
        setSneakSpread(1);
        setRange(40);

        setLegDamage(3);
        setBodyDamage(5);
        setHeadDamage(7);

        setFireRate(120);

        setMaxAmmo(7);

        setReloadTimeInTicks(20);

        registerSound(GunSoundType.SHOOT, "shoot.pistol", 1.5f, 1);

        generateDefaultLore();


    }
}
