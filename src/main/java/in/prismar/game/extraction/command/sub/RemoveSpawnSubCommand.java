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
public class RemoveSpawnSubCommand extends HelpSubCommand<Player> {

    private ExtractionFacade facade;

    public RemoveSpawnSubCommand(ExtractionFacade facade) {
        super("removespawn");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "extraction.admin");
        setDescription("Remove latest spawn");
        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(facade.getMapFile().getEntity().getSpawns().isEmpty()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cNothing to remove");
            return true;
        }
        player.sendMessage(PrismarinConstants.PREFIX + "You have removed the latest §3spawn");
        facade.getMapFile().removeLatestSpawn();
        return true;
    }
}
