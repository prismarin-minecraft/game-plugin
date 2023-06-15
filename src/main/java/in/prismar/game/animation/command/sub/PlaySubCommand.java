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
public class PlaySubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public PlaySubCommand(AnimationFacade facade) {
        super("play");
        this.facade = facade;
        setUsage("<id> <[reverse]>");
        setDescription("Play animation");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);

            if(!facade.getRepository().existsById(id.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis animation does not exists");
                return true;
            }
            if(arguments.getLength() >= 3) {
                final boolean reverse = arguments.getBoolean(2);
                facade.getService().play(facade.getRepository().findById(id.toLowerCase()), reverse);
            }  else {
                facade.getService().play(facade.getRepository().findById(id.toLowerCase()), false);
            }

            player.sendMessage(PrismarinConstants.PREFIX + "§7Playing animation §a" + id.toLowerCase());
            return true;
        }
        return false;
    }
}
