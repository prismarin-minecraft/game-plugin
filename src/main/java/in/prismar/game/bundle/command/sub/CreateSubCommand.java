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
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public CreateSubCommand(BundleFacade facade) {
        super("create");
        this.facade = facade;
        setDescription("Create a new bundle");
        setUsage("<id> <display>");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis bundle already exists");
                return true;
            }
            final String display = arguments.getCombinedArgsFrom(2).replace("&", "§");
            facade.getRepository().create(id, display);
            player.sendMessage(PrismarinConstants.PREFIX + "You successfully created the bundle §b" + id);
            return true;
        }
        return false;
    }
}
