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
public class TestGun extends Gun {

    public TestGun() {
        super("Test", GunType.SNIPER, Material.DIAMOND_HOE, "§7Test");
        setCustomModelData(1);
        setBulletsPerShot(2);
        setZoom(8);
        setAmmoType(AmmoType.SNIPER);
        setSpread(0);
        setSneakSpread(0);
        setRange(175);

        setLegDamage(4);
        setBodyDamage(4);
        setHeadDamage(4);

        setFireRate(60);

        setUnlimitedAmmo(true);
        setMaxAmmo(100);

        setReloadTimeInTicks(10);

        registerSound(GunSoundType.SHOOT, "shoot.pistol", 1, 1);

        generateDefaultLore();


    }
}
