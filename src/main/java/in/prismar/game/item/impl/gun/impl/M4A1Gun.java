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
public class M4A1Gun extends Gun {

    public M4A1Gun() {
        super("M4A1", GunType.AR, Material.NETHERITE_PICKAXE, "§9M4A1");
        setCustomModelData(1);
        setZoom(2);
        setAmmoType(AmmoType.AR);
        setSpread(2);
        setSneakSpread(1);
        setRange(55);

        setLegDamage(7);
        setBodyDamage(8);
        setHeadDamage(9);

        setFireRate(415);

        setMaxAmmo(30);

        setReloadTimeInTicks(50);

        generateDefaultLore();


    }
}
