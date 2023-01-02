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
public class LeaveSubCommand extends HelpSubCommand<Player> {

    private ExtractionFacade facade;

    public LeaveSubCommand(ExtractionFacade facade) {
        super("leave");
        setDescription("Leave extraction");
        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(!facade.isInSafeZone(player) && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "extraction.leave")) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not inside a safezone.");
            return true;
        }
        facade.leave(player);
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have left §cextraction");
        return true;
    }
}
