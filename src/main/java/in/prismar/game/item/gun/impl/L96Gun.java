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
public class L96Gun extends Gun {

    public L96Gun() {
        super("L96", GunType.SNIPER, Material.GOLDEN_SHOVEL, "§cL69");
        setAmmoType(AmmoType.SNIPER);
        setSpread(10);
        setSneakSpread(0);

        setRange(70);

        setLegDamage(10);
        setBodyDamage(15);
        setHeadDamage(20);

        setFireRate(60);

        setMaxAmmo(5);

        setReloadTimeInTicks(80);

        generateDefaultLore();

    }
}
