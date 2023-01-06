package in.prismar.game.item.impl.armor.heavy;

import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.armor.ArmorType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HeavyLeggings extends ArmorItem {
    public HeavyLeggings() {
        super("HeavyLeggings", Material.IRON_LEGGINGS, "§cHeavy Leggings", ArmorType.LEGGINGS);
        setHeadProtection(0);
        setBodyProtection(12);

        generateDefaultLore();
    }
}
