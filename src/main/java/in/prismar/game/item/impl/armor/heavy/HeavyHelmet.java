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
public class HeavyHelmet extends ArmorItem {
    public HeavyHelmet() {
        super("HeavyHelmet", Material.IRON_HELMET, "§cHeavy Helmet", ArmorType.HELMET);
        setHeadProtection(30);
        setBodyProtection(0);

        generateDefaultLore();


    }
}
