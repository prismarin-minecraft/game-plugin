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
public class MP5Gun extends Gun {

    public MP5Gun() {
        super("MP5", GunType.SMG, Material.IRON_AXE, "§eMP5");
        setAmmoType(AmmoType.SMG);
        setSpread(3.2);
        setSneakSpread(1.2);
        setRange(35);

        setLegDamage(5);
        setBodyDamage(7);
        setHeadDamage(9);

        setFireRate(625);

        setMaxAmmo(35);

        setReloadTimeInTicks(50);

        generateDefaultLore();


    }
}
