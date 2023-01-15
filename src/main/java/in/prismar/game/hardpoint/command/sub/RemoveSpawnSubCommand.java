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
public class RemoveSpawnSubCommand extends HelpSubCommand<Player> {

    private final HardpointFacade facade;

    public RemoveSpawnSubCommand(HardpointFacade facade) {
        super("removespawn");
        this.facade = facade;

        setPermission(PrismarinConstants.PERMISSION_PREFIX + "hardpoint.admin");
        setUsage("<team>");
        setDescription("Remove latest spawn from team");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            HardpointTeam team = arguments.getEnumType(HardpointTeam.values(), 1);
            if(facade.getConfigFile().getEntity().getSpawns(team).isEmpty()) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cNothing to remove.");
                return true;
            }
            facade.getConfigFile().getEntity().getSpawns(team).remove(facade.getConfigFile().getEntity().getSpawns(team).size() - 1);
            facade.getConfigFile().save();
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have removed the latest spawn from team " + team.getFancyName());
            return true;
        }
        return false;
    }
}
