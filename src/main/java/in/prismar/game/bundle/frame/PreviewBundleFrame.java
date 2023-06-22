package in.prismar.game.bundle.frame;

import in.prismar.api.user.User;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.FrameButton;
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
public class PreviewBundleFrame extends Frame {

    private static final ItemStack GLASS = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§a ").build();

    private final BundleFacade facade;
    private final User user;
    private final Player player;


    public PreviewBundleFrame(BundleFacade facade, User user, Player player, Bundle bundle) {
        super(bundle.getDisplayName(), 5);
        this.player = player;
        this.facade = facade;
        this.user = user;

        if(bundle.getContainer() != null) {
            int slot = 0;
            for(ItemStack stack : bundle.getContainer().getItem()) {
                if(stack != null) {
                    if(stack.getType() != Material.AIR) {
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
            BundleFrame frame = new BundleFrame(facade, user, player);
            frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
        });

        build();
    }
}
