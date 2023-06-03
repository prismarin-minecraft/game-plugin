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
        super("Test", GunType.PISTOL, Material.WOODEN_HOE, "§7Test");
        setCustomModelData(4);
        setBulletsPerShot(1);
        setZoom(3);
        setAmmoType(AmmoType.PISTOL);
        setSpread(0);
        setSneakSpread(0);
        setRange(30);

        setRecoil(0.35);

        setLegDamage(4);
        setBodyDamage(4);
        setHeadDamage(4);

        setFireRate(600);

        setUnlimitedAmmo(true);
        setMaxAmmo(100);

        setReloadTimeInTicks(20 * 5);

        registerSound(GunSoundType.SHOOT, "shoot.pistol", 1, 1);
        registerSound(GunSoundType.RELOAD_IN, "reload.pistol.clipin", 1, 1);
        registerSound(GunSoundType.RELOAD_OUT, "reload.pistol.clipout", 1, 1);

        generateDefaultLore();


        buildStateItems();
    }
}
