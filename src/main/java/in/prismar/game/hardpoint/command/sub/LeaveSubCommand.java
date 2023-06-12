package in.prismar.game.hardpoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.hardpoint.HardpointFacade;
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

    private final HardpointFacade facade;

    public LeaveSubCommand(HardpointFacade facade) {
        super("leave");
        setDescription("Leave the game");
        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(!facade.isCurrentlyPlaying(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not playing.");
            return true;
        }
        facade.leave(player);
        player.sendMessage(PrismarinConstants.PREFIX + "§7You left the game.");
        return true;
    }
}
