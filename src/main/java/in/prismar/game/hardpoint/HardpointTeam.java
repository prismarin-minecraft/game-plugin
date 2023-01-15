package in.prismar.game.hardpoint;

import in.prismar.library.spigot.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public enum HardpointTeam {

    RED(Color.RED, new ItemBuilder(Material.RED_WOOL).setName("§cRed").allFlags().build()),

    BLUE(Color.BLUE, new ItemBuilder(Material.BLUE_WOOL).setName("§9Blue").allFlags().build());

    private final Color color;
    private final ItemStack icon;

    public String getFancyName() {
        return icon.getItemMeta().getDisplayName();
    }
}
