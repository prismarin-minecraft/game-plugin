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
public class P2020Gun extends Gun {

    public P2020Gun() {
        super("P2020", GunType.PISTOL, Material.STICK, "§fP2020");
        setAmmoType(AmmoType.PISTOL);
        setSpread(3);
        setSneakSpread(0.5);
        setRange(20);

        setLegDamage(2);
        setBodyDamage(6);
        setHeadDamage(7);

        setFireRate(130);

        setMaxAmmo(10);

        setReloadTimeInTicks(20);

        generateDefaultLore();


    }
}
