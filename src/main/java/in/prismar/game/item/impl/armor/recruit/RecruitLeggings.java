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
public class RecruitLeggings extends ArmorItem {
    public RecruitLeggings() {
        super("RecruitLeggings", Material.LEATHER_LEGGINGS, "§2Recruit Leggings", ArmorType.LEGGINGS);
        setHeadProtection(0);
        setBodyProtection(6);
        generateDefaultLore();
    }
}
