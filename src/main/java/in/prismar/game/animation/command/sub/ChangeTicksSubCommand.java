package in.prismar.game.animation.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.animation.AnimationFacade;
import in.prismar.game.animation.model.Animation;
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
public class ChangeTicksSubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public ChangeTicksSubCommand(AnimationFacade facade) {
        super("changeticks");
        this.facade = facade;
        setAliases("changetick");
        setUsage("<id> <ticks>");
        setDescription("Change animation ticks");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);

            if(!facade.getRepository().existsById(id.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis animation does not exists");
                return true;
            }

            final int ticks = arguments.getInteger(2);
            Animation animation = facade.getRepository().findById(id.toLowerCase());
            facade.getService().changeTicks(animation, ticks);
            player.sendMessage(PrismarinConstants.PREFIX + "§7Changed animation ticks to §a" + ticks);
            return true;
        }
        return false;
    }
}
