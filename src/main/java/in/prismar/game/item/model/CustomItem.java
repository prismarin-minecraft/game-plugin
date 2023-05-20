package in.prismar.game.item.model;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEventBus;
import lombok.Data;
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

    private int customModelData = -1;

    private boolean unbreakable;
    private List<String> lore;

    private CustomItemEventBus eventBus;

    private ItemFlag[] flags;
    public CustomItem(String id, Material material, String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.material = material;
        this.eventBus = new CustomItemEventBus(this);
    }

    public CustomItem setCustomModelData(int value) {
        this.customModelData = value;
        return this;
    }

    public CustomItem setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
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
        if(customModelData != -1) {
            meta.setCustomModelData(customModelData);
        }
        if(displayName != null) {
            meta.setDisplayName(displayName);
        }
        if(lore != null) {
            meta.setLore(lore);
        }
        if (this.flags != null) {
            meta.addItemFlags(this.flags);
        }
        meta.setUnbreakable(unbreakable);
        stack.setItemMeta(meta);
        return stack;
    }

    public void onBuild(Game game, ItemStack stack) {}
}
