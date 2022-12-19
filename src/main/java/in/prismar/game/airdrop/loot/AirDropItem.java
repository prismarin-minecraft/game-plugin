package in.prismar.game.airdrop.loot;

import in.prismar.library.spigot.item.container.ItemContainer;
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
public class AirDropItem {
    private ItemContainer item;
    private double chance;
}
