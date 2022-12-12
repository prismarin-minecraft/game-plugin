package in.prismar.game.bundle.command.sub;

import com.google.common.base.Joiner;
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
public class ListSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public ListSubCommand(BundleFacade facade) {
        super("list");
        this.facade = facade;
        setDescription("List all bundles");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "Bundles§8: §b" + Joiner.on("§8, §b").join(facade.getRepository().findAll()));
        return true;
    }
}
