package in.prismar.game.item.impl.armor.misc;

import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.armor.ArmorType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GasMaskHelmet extends ArmorItem {
    public GasMaskHelmet() {
        super("GasMask", Material.BOWL, "§2Gas Mask", ArmorType.HELMET);
        setHeadProtection(25);
        setBodyProtection(0);
        setCustomModelData(4);
        setCustom(true);
        generateDefaultLore();
        addLore("§c ");
        addLore(" §7Protects against harmful gases");
        addLore("§c ");

    }

}
