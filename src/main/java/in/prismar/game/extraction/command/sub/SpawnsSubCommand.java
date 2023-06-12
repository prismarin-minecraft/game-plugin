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
public class SpawnsSubCommand extends HelpSubCommand<Player> {

    private final ExtractionFacade facade;

    public SpawnsSubCommand(ExtractionFacade facade) {
        super("spawns");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "extraction.admin");
        setDescription("Gives you the spawns amount");
        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "There are currently §b" + facade.getMapFile().getEntity().getSpawns().size() + " spawns");
        return true;
    }
}
