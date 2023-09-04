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
public class PortableMinerItem extends CustomItem {
    public PortableMinerItem() {
        super("portableminer", Material.PLAYER_HEAD, "§6Portable Miner Shop");
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "shopopen " + player.getName() + " miner");
    }

    @Override
    public ItemStack build() {
        return new CustomSkullBuilder("https://textures.minecraft.net/texture/f9dc48ba5326a4078d731cb00441e62ba9402400c0d175b9ac734d2d52cf2329").setName(getDisplayName()).allFlags().build();
    }
}
