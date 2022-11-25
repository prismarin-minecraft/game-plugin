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
public class FnScarGun extends Gun {

    public FnScarGun() {
        super("FnScar", GunType.SMG, Material.GOLDEN_PICKAXE, "§9Fn Scar");
        setAmmoType(AmmoType.AR);
        setSpread(2.5);
        setSneakSpread(1);
        setRange(55);

        setLegDamage(4);
        setBodyDamage(8);
        setHeadDamage(10);

        setFireRate(375);

        setMaxAmmo(35);

        setReloadTimeInTicks(45);

        generateDefaultLore();


    }
}
