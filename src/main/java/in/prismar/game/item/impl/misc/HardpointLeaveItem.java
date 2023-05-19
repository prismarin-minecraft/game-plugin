package in.prismar.game.item.impl.misc;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HardpointLeaveItem extends CustomItem {

    public HardpointLeaveItem() {
        super("HardpointLeave", Material.OAK_DOOR, "§cLeave Hardpoint");
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
        player.performCommand("hardpoint leave");
    }

}
