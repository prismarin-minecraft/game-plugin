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
public class G36Gun extends Gun {

    public G36Gun() {
        super("G36", GunType.AR, Material.IRON_PICKAXE, "§9G36");
        setAmmoType(AmmoType.AR);
        setSpread(2.5);
        setSneakSpread(1);
        setRange(50);

        setLegDamage(3);
        setBodyDamage(6);
        setHeadDamage(9);

        setFireRate(400);

        setMaxAmmo(30);

        setReloadTimeInTicks(40);

        generateDefaultLore();


    }
}
