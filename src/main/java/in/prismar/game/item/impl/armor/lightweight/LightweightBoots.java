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
public class LightweightBoots extends ArmorItem {
    public LightweightBoots() {
        super("LightweightBoots", Material.CHAINMAIL_BOOTS, "§7Lightweight Boots", ArmorType.BOOTS);
        setHeadProtection(0);
        setBodyProtection(4);

        generateDefaultLore();
    }
}
