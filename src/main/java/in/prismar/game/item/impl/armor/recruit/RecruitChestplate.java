package in.prismar.game.item.impl.armor.recruit;

import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.armor.ArmorType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RecruitChestplate extends ArmorItem {
    public RecruitChestplate() {
        super("RecruitChestplate", Material.LEATHER_CHESTPLATE, "§2Recruit Chestplate", ArmorType.CHESTPLATE);
        setHeadProtection(0);
        setBodyProtection(5);

        generateDefaultLore();
    }
}
