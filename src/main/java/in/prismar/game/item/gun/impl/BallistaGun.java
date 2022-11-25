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
public class BallistaGun extends Gun {

    public BallistaGun() {
        super("Ballista", GunType.SNIPER, Material.IRON_HOE, "§cBallista");
        setAmmoType(AmmoType.SNIPER);
        setSpread(10);
        setSneakSpread(0);

        setRange(70);

        setLegDamage(14);
        setBodyDamage(18);
        setHeadDamage(26);

        setFireRate(60);

        setMaxAmmo(7);

        setReloadTimeInTicks(80);

        generateDefaultLore();

    }
}
