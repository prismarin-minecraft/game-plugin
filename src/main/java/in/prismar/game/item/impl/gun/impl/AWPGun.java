package in.prismar.game.item.impl.gun.impl;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AWPGun extends Gun {

    public AWPGun() {
        super("AWP", GunType.SNIPER, Material.STONE_HOE, "§cAWP");
        setCustomModelData(1);
        setZoom(8);
        setAmmoType(AmmoType.SNIPER);
        setSpread(16);
        setSneakSpread(0);
        setRange(175);

        setLegDamage(15);
        setBodyDamage(20);
        setHeadDamage(30);

        setFireRate(60);

        setMaxAmmo(5);

        setReloadTimeInTicks(70);

        registerSound(GunSoundType.SHOOT, "shoot.sniper", 4, 1);

        generateDefaultLore();


    }
}
