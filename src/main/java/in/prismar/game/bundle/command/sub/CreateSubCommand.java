package in.prismar.game.bundle.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public CreateSubCommand(BundleFacade facade) {
        super("create");
        this.facade = facade;
        setDescription("Create a new bundle");
        setUsage("<id> <seasonal> <display>");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "bundle.admin");

    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 4) {
            final String id = arguments.getString(1);
            if(facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis bundle already exists");
                return true;
            }
            final boolean seasonal = arguments.getBoolean(2);
            ItemStack icon = null;
            if(seasonal) {
                if(!ItemUtil.hasItemInHandAndHasDisplayName(player, true)) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold an item with a display name in your hand.");
                    return true;
                }
                icon = player.getInventory().getItemInMainHand();
            }
            final String display = arguments.getCombinedArgsFrom(3).replace("&", "§");
            facade.getRepository().create(id, display, seasonal, icon);
            player.sendMessage(PrismarinConstants.PREFIX + "You successfully created the bundle §b" + id);
            return true;
        }
        return false;
    }
}
