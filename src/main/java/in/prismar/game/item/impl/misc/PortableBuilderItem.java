package in.prismar.game.item.impl.misc;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.CustomSkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PortableBuilderItem extends CustomItem {
    public PortableBuilderItem() {
        super("portablebuilder", Material.PLAYER_HEAD, "§dPortable Builder");
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shopopen " + player.getName() + " builder");
    }

    @Override
    public ItemStack build() {
        return new CustomSkullBuilder("https://textures.minecraft.net/texture/9d8ee98a4a5750ef5b78fc6044f7979e90d0c3125b817a9eb8a62a1adcf3eb1d").setName(getDisplayName()).allFlags().build();
    }
}
