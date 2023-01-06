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
public class LightweightChestplate extends ArmorItem {
    public LightweightChestplate() {
        super("LightweightChestplate", Material.CHAINMAIL_CHESTPLATE, "§7Lightweight Chestplate", ArmorType.CHESTPLATE);
        setHeadProtection(0);
        setBodyProtection(10);

        generateDefaultLore();
    }
}
