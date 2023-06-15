package in.prismar.game.animation.command.sub;

import com.google.common.base.Joiner;
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
public class ListSubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public ListSubCommand(AnimationFacade facade) {
        super("list");
        this.facade = facade;
        setDescription("List all animations");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7Animations§8: §a" + Joiner.on("§8, §a").join(facade.getRepository().getAnimations()));
        return true;
    }
}
