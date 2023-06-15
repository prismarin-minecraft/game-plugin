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
public class SaveFrameSubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public SaveFrameSubCommand(AnimationFacade facade) {
        super("saveFrame");
        this.facade = facade;
        setUsage("<id> <index>");
        setDescription("Save frame to index");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);

            if(!facade.getRepository().existsById(id.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis animation does not exists");
                return true;
            }

            Animation animation = facade.getRepository().findById(id.toLowerCase());
            final int index = arguments.getInteger(2);
            if(!facade.getService().existsFrame(animation, index)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis index does not exists");
                return true;
            }
            facade.getService().saveFrame(animation, index);
            player.sendMessage(PrismarinConstants.PREFIX + "§7Successfully saved frame §8(§a" + (index) + "§8)");
            return true;
        }
        return false;
    }
}
