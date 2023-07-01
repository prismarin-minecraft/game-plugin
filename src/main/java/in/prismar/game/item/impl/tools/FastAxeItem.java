package in.prismar.game.item.impl.tools;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.CustomSkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FastAxeItem extends CustomItem {

    protected RegionProvider regionProvider;

    public FastAxeItem() {
        super("fastaxe", Material.NETHERITE_AXE, "§cFast Axe");
        addLore("§c");
        addLore(PrismarinConstants.ARROW_RIGHT + " §7This axe can only be used in the §clumbering area");
        addLore("§c");

        allFlags();

        setUnbreakable(true);
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(regionProvider == null) {
                regionProvider = PrismarinApi.getProvider(RegionProvider.class);
            }
            if(!regionProvider.isIn(event.getClickedBlock().getLocation(), "lumber")) {
                return;
            }
            BlockBreakEvent breakEvent = new BlockBreakEvent(event.getClickedBlock(), player);
            Bukkit.getPluginManager().callEvent(breakEvent);
        }
    }

}
