package in.prismar.game.item;

import in.prismar.game.item.event.CustomItemEventBus;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class CustomItem {

    private final String id;
    private final Material material;

    private final String displayName;
    private List<String> lore;

    private CustomItemEventBus eventBus;

    private ItemFlag[] flags;
    public CustomItem(String id, Material material, String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.eventBus = new CustomItemEventBus(this);
    }

    public CustomItem allFlags() {
        this.flags = new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE};
        return this;
    }

    public CustomItem addLore(String... lore) {
        if(this.lore == null) {
            this.lore = new ArrayList<>();
        }
        this.lore.addAll(Arrays.asList(lore));
        return this;
    }

    public ItemStack build() {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        if(displayName != null) {
            meta.setDisplayName(displayName);
        }
        if(lore != null) {
            meta.setLore(lore);
        }
        if (this.flags != null) {
            meta.addItemFlags(this.flags);
        }
        stack.setItemMeta(meta);
        return stack;
    }
}
