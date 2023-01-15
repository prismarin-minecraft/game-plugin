package in.prismar.game.hardpoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.hardpoint.HardpointTeam;
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

    private final HardpointFacade facade;

    public AddSpawnSubCommand(HardpointFacade facade) {
        super("addspawn");
        this.facade = facade;

        setPermission(PrismarinConstants.PERMISSION_PREFIX + "hardpoint.admin");
        setUsage("<team>");
        setDescription("Add a spawn to a team");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            HardpointTeam team = arguments.getEnumType(HardpointTeam.values(), 1);
            facade.getConfigFile().getEntity().getSpawns(team).add(player.getLocation());
            facade.getConfigFile().save();

            player.sendMessage(PrismarinConstants.PREFIX + "§7You have added a new spawn to team " + team.getFancyName());
            return true;
        }
        return false;
    }
}
