package in.prismar.game.item.impl.gun.impl;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DesertEagleGun extends Gun {

    public DesertEagleGun() {
        super("DesertEagle", GunType.PISTOL, Material.BLAZE_ROD, "§fDesert Eagle");
        setAmmoType(AmmoType.PISTOL);
        setSpread(1.5);
        setSneakSpread(0);
        setRange(45);

        setLegDamage(6);
        setBodyDamage(12);
        setHeadDamage(18);

        setFireRate(60);

        setMaxAmmo(7);

        setReloadTimeInTicks(45);

        generateDefaultLore();


    }
}
