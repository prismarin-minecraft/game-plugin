package in.prismar.game.weapon;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Gun {

    private String id;
    private ItemStack item;

    private GunType type;
    private int fireRate = 120;
    private double spread = 1;
    private int bulletsPerShot = 1;

    private int maxAmmo = 10;
    private long reloadTime = 1000;

    private double legDamage = 2;
    private double bodyDamage = 4;
    private double headDamage = 8;


    public Gun(String id, GunType type, ItemStack item) {
        this.id = id;
        this.type = type;
        this.item = item;

    }

    public void shoot() {

    }
}
