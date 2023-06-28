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
public class LightweightLeggings extends ArmorItem {
    public LightweightLeggings() {
        super("LightweightLeggings", Material.CHAINMAIL_LEGGINGS, "§7Lightweight Leggings", ArmorType.LEGGINGS);
        setHeadProtection(0);
        setBodyProtection(7);
        generateDefaultLore();
    }
}
