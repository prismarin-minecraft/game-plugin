package in.prismar.game.item.impl.armor.hardpoint;

import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.armor.ArmorType;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HardpointBoots extends ArmorItem {

    private Color color;

    public HardpointBoots(String name, Color color) {
        super("HardpointBoots" + name, Material.LEATHER_BOOTS, name + " Boots", ArmorType.BOOTS);
        this.color = color;
        setHeadProtection(0);
        setBodyProtection(4);

        generateDefaultLore();
    }

    @Override
    public ItemStack build() {
        ItemStack stack = super.build();
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return stack;
    }
}
