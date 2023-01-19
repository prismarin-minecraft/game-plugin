package in.prismar.game.item.impl.placeable;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import in.prismar.library.spigot.item.ItemUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public abstract class PlaceableItem extends CustomItem {

    @Setter
    private ItemStack placedItem;

    public PlaceableItem(String id, Material material, String displayName) {
        super(id, material, displayName);
        allFlags();
        this.placedItem = build();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        if(game.getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp")) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not allowed to use this item inside a safe region.");
            return;
        }
        if(!isAllowedToPlace(player, game)) {
            return;
        }
        ItemUtil.takeItemFromHand(player, true);
        Hologram hologram = new Hologram(player.getLocation().clone().subtract(0, 1.7, 0));
        hologram.addLine(HologramLineType.ITEM_HEAD, placedItem);
        hologram.enable();

        onPlace(player, game, hologram);
    }

    public abstract void onPlace(Player player, Game game, Hologram hologram);
    public boolean isAllowedToPlace(Player player, Game game){
        return true;
    }
}
