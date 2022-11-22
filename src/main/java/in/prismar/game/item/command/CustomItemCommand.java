package in.prismar.game.item.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class CustomItemCommand extends SpigotCommand<Player> {

    @Inject
    private CustomItemRegistry registry;


    public CustomItemCommand() {
        super("customitem");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "customitem");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 1) {
            final String id = arguments.getString(0);
            if(!registry.existsItemById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis item does not exists.");
                return true;
            }
            CustomItem customItem = registry.getItemById(id);
            player.getInventory().addItem(customItem.build());
            player.sendMessage(PrismarinConstants.PREFIX + "§7You received the item §a" + customItem.getDisplayName());
            return true;
        }
        player.sendMessage(PrismarinConstants.PREFIX + "Items§8:");
        for(CustomItem item : registry.getItems().values()) {
            player.sendMessage(PrismarinConstants.PREFIX + "  §8- §a" + item.getId());
        }
        return true;
    }


}
