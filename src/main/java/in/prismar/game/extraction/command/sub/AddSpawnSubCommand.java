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
public class AddSpawnSubCommand extends HelpSubCommand<Player> {

    private ExtractionFacade facade;

    public AddSpawnSubCommand(ExtractionFacade facade) {
        super("addspawn");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "extraction.admin");
        setDescription("Add a spawn");
        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "You have added a new §bspawn");
        facade.getMapFile().addSpawn(player.getLocation());
        return true;
    }
}
