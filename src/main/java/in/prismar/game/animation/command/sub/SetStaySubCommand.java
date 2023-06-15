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
public class SetStaySubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public SetStaySubCommand(AnimationFacade facade) {
        super("setstay");
        this.facade = facade;
        setAliases("setstay");
        setUsage("<id> <true|false>");
        setDescription("Let end stay");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);

            if(!facade.getRepository().existsById(id.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis animation does not exists");
                return true;
            }
            final boolean stay = arguments.getBoolean(2);

            Animation animation = facade.getRepository().findById(id.toLowerCase());
            facade.getService().setStay(animation, stay);
            player.sendMessage(PrismarinConstants.PREFIX + "§7Changed animation stay to §a" + stay);
            return true;
        }
        return false;
    }
}
