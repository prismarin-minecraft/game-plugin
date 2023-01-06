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
public class HeavyBoots extends ArmorItem {
    public HeavyBoots() {
        super("HeavyBoots", Material.IRON_BOOTS, "§cHeavy Boots", ArmorType.BOOTS);
        setHeadProtection(0);
        setBodyProtection(6);

        generateDefaultLore();
    }
}
