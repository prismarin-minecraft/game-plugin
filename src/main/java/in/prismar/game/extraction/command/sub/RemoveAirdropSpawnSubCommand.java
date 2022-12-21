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
public class RemoveAirdropSpawnSubCommand extends HelpSubCommand<Player> {

    private ExtractionFacade facade;

    public RemoveAirdropSpawnSubCommand(ExtractionFacade facade) {
        super("removeairdropspawn");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "extraction.admin");
        setDescription("Remove latest airdrop spawn");
        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(facade.getMapFile().getEntity().getAirdropLocations().isEmpty()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cNothing to remove");
            return true;
        }
        player.sendMessage(PrismarinConstants.PREFIX + "You have removed the latest §3airdrop spawn");
        facade.getMapFile().removeLatestAirdrop();
        return true;
    }
}
