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
public class RecruitHelmet extends ArmorItem {
    public RecruitHelmet() {
        super("RecruitHelmet", Material.LEATHER_HELMET, "§2Recruit Helmet", ArmorType.HELMET);
        setHeadProtection(5);
        setBodyProtection(0);
        generateDefaultLore();
    }
}
