package in.prismar.game.animation.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.animation.AnimationFacade;
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

    private final AnimationFacade facade;

    public DeleteSubCommand(AnimationFacade facade) {
        super("delete");
        this.facade = facade;
        setUsage("<id>");
        setDescription("Delete animation");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);

            if(!facade.getRepository().existsById(id.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis animation does not exists");
                return true;
            }
            facade.getService().delete(id);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have deleted the animation §e" + id.toLowerCase());
            return true;
        }
        return false;
    }
}
