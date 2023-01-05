package in.prismar.game.item.impl.meele;

import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class KatanaItem extends MeeleItem {
    public KatanaItem() {
        super("Katana", Material.IRON_SWORD, "§fKatana");
        setAttackSpeed(-3);
        setDamage(8);
        generateDefaultLore();
    }
}
