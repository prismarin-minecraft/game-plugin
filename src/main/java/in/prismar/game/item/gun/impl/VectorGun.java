package in.prismar.game.item.gun.impl;

import in.prismar.game.item.gun.Gun;
import in.prismar.game.item.gun.type.AmmoType;
import in.prismar.game.item.gun.type.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class VectorGun extends Gun {

    public VectorGun() {
        super("Vector", GunType.SMG, Material.GOLDEN_AXE, "§eVector");
        setAmmoType(AmmoType.SMG);
        setSpread(3);
        setSneakSpread(1.5);
        setRange(30);

        setLegDamage(2);
        setBodyDamage(5);
        setHeadDamage(8);

        setFireRate(750);

        setMaxAmmo(25);

        setReloadTimeInTicks(20);

        generateDefaultLore();


    }
}
