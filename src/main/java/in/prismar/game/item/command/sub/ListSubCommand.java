package in.prismar.game.item.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.template.Pager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ListSubCommand extends HelpSubCommand<Player> {

    private final CustomItemRegistry registry;

    public ListSubCommand(CustomItemRegistry registry) {
        super("list");
        setDescription("View all custom items");
        setAliases("l");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments spigotArguments) throws CommandException {
        openPager(player);
        return true;
    }

    private Pager openPager(Player player) {
        Pager pager = new Pager("§6Custom Items" , 6);
        pager.fill();
        for(Map.Entry<String, CustomItem> entry : registry.getItems().entrySet()) {
            ItemStack clone = entry.getValue().build();
            ItemMeta meta = clone.getItemMeta();
            List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            lore.add("§c");
            lore.add(" §8" + PrismarinConstants.DOT + " §3Click §7to retrieve this item");
            lore.add("§c");
            meta.setLore(lore);
            clone.setItemMeta(meta);
            pager.addItem(clone, (ClickFrameButtonEvent) (player1, event) -> {
                player.performCommand("customitem get " + entry.getKey());
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.65F, 1);
            });
        }

        pager.build();
        pager.openInventory(player, Sound.UI_BUTTON_CLICK, 0.65f);
        return pager;
    }
}
