package in.prismar.game.item.holder;

import in.prismar.game.item.CustomItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class CustomItemHolder {

    private CustomItem item;
    private ItemStack stack;
    private CustomItemHoldingType holdingType;
}
