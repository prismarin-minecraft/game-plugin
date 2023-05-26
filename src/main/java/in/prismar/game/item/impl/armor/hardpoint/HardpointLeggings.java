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
public class HardpointLeggings extends ArmorItem {

    private Color color;

    public HardpointLeggings(String name, Color color) {
        super("HardpointLeggings" + name, Material.LEATHER_LEGGINGS, name + " Leggings", ArmorType.LEGGINGS);
        this.color = color;
        setHeadProtection(0);
        setBodyProtection(6);

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
