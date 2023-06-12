package in.prismar.game.extraction.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.extraction.ExtractionFacade;
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
public class JoinSubCommand extends HelpSubCommand<Player> {

    private final ExtractionFacade facade;

    public JoinSubCommand(ExtractionFacade facade) {
        super("join");
        setDescription("Join extraction");
        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(!facade.isRunning()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cExtraction is currently not open");
            return true;
        }
        if(facade.isIn(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already inside extraction.");
            return true;
        }
        facade.join(player);
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have joined §cextraction");
        return true;
    }
}
