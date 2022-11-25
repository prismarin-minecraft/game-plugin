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
public class M4A1Gun extends Gun {

    public M4A1Gun() {
        super("M4A1", GunType.AR, Material.NETHERITE_PICKAXE, "§9M4A1");
        setAmmoType(AmmoType.AR);
        setSpread(2);
        setSneakSpread(1);
        setRange(55);

        setLegDamage(7);
        setBodyDamage(9);
        setHeadDamage(12);

        setFireRate(415);

        setMaxAmmo(30);

        setReloadTimeInTicks(50);

        generateDefaultLore();


    }
}
