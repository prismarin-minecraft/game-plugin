package in.prismar.game.bundle.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DeleteSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public DeleteSubCommand(BundleFacade facade) {
        super("delete");
        this.facade = facade;
        setDescription("Delete a bundle");
        setUsage("<id>");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "bundle.admin");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis bundle does exists");
                return true;
            }
            facade.getRepository().deleteById(id);
            player.sendMessage(PrismarinConstants.PREFIX + "You deleted the bundle §3" + id);
            return true;
        }
        return false;
    }
}
