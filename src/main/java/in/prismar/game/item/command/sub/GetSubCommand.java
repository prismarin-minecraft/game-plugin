package in.prismar.game.item.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GetSubCommand extends HelpSubCommand<Player> {

    private final CustomItemRegistry registry;

    public GetSubCommand(CustomItemRegistry registry) {
        super("get");
        setDescription("Get an item");
        setAliases("g");
        setUsage("<id>");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!registry.existsItemById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis item does not exists.");
                return true;
            }
            CustomItem customItem = registry.getItemById(id);
            ItemStack stack = registry.createItem(id);

            if(arguments.getLength() >= 3) {
                ItemBuilder builder = new ItemBuilder(stack.getType());
                if(stack.hasItemMeta()) {
                    if(stack.getItemMeta().hasCustomModelData()) {
                        builder.setCustomModelData(stack.getItemMeta().getCustomModelData());
                    }
                }
                stack = builder.build();
            }

            player.getInventory().addItem(stack);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You received the item §a" + customItem.getDisplayName());
            return true;
        }
        return false;
    }
}
