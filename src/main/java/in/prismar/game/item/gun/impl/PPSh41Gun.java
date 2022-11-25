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
public class PPSh41Gun extends Gun {

    public PPSh41Gun() {
        super("PPSh-41", GunType.SMG, Material.NETHERITE_AXE, "§ePPSh-41");
        setAmmoType(AmmoType.SMG);
        setSpread(2.5);
        setSneakSpread(1);
        setRange(45);

        setLegDamage(6);
        setBodyDamage(7);
        setHeadDamage(8);

        setFireRate(500);

        setMaxAmmo(42);

        setReloadTimeInTicks(70);

        generateDefaultLore();


    }
}
