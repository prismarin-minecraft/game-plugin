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
public class AWPGun extends Gun {

    public AWPGun() {
        super("AWP", GunType.SNIPER, Material.SPYGLASS, "§cAWP");
        setAmmoType(AmmoType.SNIPER);
        setSpread(12);
        setSneakSpread(0);

        setRange(80);

        setLegDamage(17);
        setBodyDamage(20);
        setHeadDamage(30);

        setFireRate(40);

        setMaxAmmo(5);

        setReloadTimeInTicks(70);

        generateDefaultLore();

    }
}
