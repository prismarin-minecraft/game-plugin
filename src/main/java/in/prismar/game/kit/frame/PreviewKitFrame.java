package in.prismar.game.kit.frame;

import in.prismar.api.user.User;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.frame.BundleFrame;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.game.kit.KitService;
import in.prismar.game.kit.model.Kit;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PreviewKitFrame extends Frame {

    private static final ItemStack GLASS = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§a ").build();



    public PreviewKitFrame(KitService service, Kit kit) {
        super(kit.getIcon().getItem().getItemMeta().getDisplayName(), 5);

        if (kit.getItems() != null) {
            int slot = 0;
            for (ItemStack stack : kit.getItems().getItem()) {
                if (stack != null) {
                    if (stack.getType() != Material.AIR) {
                        addButton(slot, stack);
                        slot++;
                    }
                }

            }
        }

        for (int i = 27; i <= 35; i++) {
            addButton(i, GLASS);
        }
        addButton(40, new ItemBuilder(Material.OAK_DOOR).setName("§cBack").build(), (ClickFrameButtonEvent) (player1, event) -> {
            KitFrame frame = new KitFrame(service, player1);
            frame.openInventory(player1, Sound.UI_BUTTON_CLICK, 0.5f);
        });

        build();
    }
}
