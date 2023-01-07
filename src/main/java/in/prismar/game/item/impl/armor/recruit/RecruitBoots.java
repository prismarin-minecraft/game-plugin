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
public class RecruitBoots extends ArmorItem {
    public RecruitBoots() {
        super("RecruitBoots", Material.LEATHER_BOOTS, "§2Recruit Boots", ArmorType.BOOTS);
        setHeadProtection(0);
        setBodyProtection(2);

        generateDefaultLore();
    }
}
