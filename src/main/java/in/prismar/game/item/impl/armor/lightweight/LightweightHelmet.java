package in.prismar.game.item.impl.armor.lightweight;

import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.armor.ArmorType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LightweightHelmet extends ArmorItem {
    public LightweightHelmet() {
        super("LightweightHelmet", Material.CHAINMAIL_HELMET, "§7Lightweight Helmet", ArmorType.HELMET);
        setHeadProtection(10);
        setBodyProtection(0);
        generateDefaultLore();
    }
}
