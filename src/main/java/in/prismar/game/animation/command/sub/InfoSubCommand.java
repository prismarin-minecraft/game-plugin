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
public class InfoSubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public InfoSubCommand(AnimationFacade facade) {
        super("info");
        this.facade = facade;
        setUsage("<id>");
        setDescription("Information about animation");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);

            if(!facade.getRepository().existsById(id.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis animation does not exists");
                return true;
            }
            Animation animation = facade.getRepository().findById(id.toLowerCase());
            player.sendMessage(PrismarinConstants.PREFIX + "§7ID: §a" + animation.getId());
            player.sendMessage(PrismarinConstants.PREFIX + "§7Ticks: §6" + animation.getTicks());
            player.sendMessage(PrismarinConstants.PREFIX + "§7Stay: §3" + animation.isStay());
            player.sendMessage(PrismarinConstants.PREFIX + "§7Frames: §b" + animation.getFrames().size());
            return true;
        }
        return false;
    }
}
