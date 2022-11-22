package in.prismar.game.item.gun.impl;

import in.prismar.game.item.gun.Gun;
import in.prismar.game.item.gun.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class TestGun extends Gun {

    public TestGun() {
        super("Test", GunType.PISTOL, Material.WOODEN_HOE, "§dDanny Wanny");
        setSpread(0);

    }
}
