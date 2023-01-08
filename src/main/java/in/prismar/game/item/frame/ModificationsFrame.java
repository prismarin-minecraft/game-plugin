package in.prismar.game.item.frame;

import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.units.qual.A;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ModificationsFrame extends Frame {
    public ModificationsFrame(CustomItemRegistry registry) {
        super("§cModifications", 3);

        fill();

        addButton(11, new ItemBuilder(Material.GRAY_DYE).setName("§cAttachments")
                .addLore("§c").addLore("§7Click me to open the attachments table").build(), (ClickFrameButtonEvent) (player, event) -> {
                    AttachmentFrame frame = new AttachmentFrame(registry, null);
                    frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.7f);
                });

        addButton(15, new ItemBuilder(Material.WOODEN_PICKAXE).setCustomModelData(10500).setName("§6Skins")
                .addLore("§c").addLore("§7Click me to open skins table").build(), (ClickFrameButtonEvent) (player, event) -> {
            SkinsFrame frame = new SkinsFrame(registry, null);
            frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.7f);
        });

        build();
    }
}
