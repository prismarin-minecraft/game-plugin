package in.prismar.game.item.listener.custom;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class CustomItemClickEventListener implements Listener {

    @Inject
    private CustomItemRegistry registry;

    @Inject
    private Game game;

    @EventHandler
    public void onCall(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player player) {
            registry.publishEvent(player, event);
            if(event.getCursor() != null) {
                if(event.getSlotType() == InventoryType.SlotType.ARMOR && event.getRawSlot() == 5) {
                    if(player.getInventory().getHelmet() != null) {
                        if(player.getInventory().getHelmet().getType() != Material.AIR) {
                            return;
                        }
                    }
                    CustomItem item = registry.getItemByStack(event.getCursor());
                    if(item instanceof ArmorItem armor) {
                        if(!armor.isCustom()) {
                            return;
                        }
                        player.getInventory().setHelmet(event.getCursor().clone());
                        event.setCancelled(true);
                        event.setResult(Event.Result.DENY);
                        event.setCursor(new ItemStack(Material.AIR));
                        player.updateInventory();
                    }
                }

            }
        }

    }
}
