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
public class RemovePointSubCommand extends HelpSubCommand<Player> {

    private final HardpointFacade facade;

    public RemovePointSubCommand(HardpointFacade facade) {
        super("removepoint");
        this.facade = facade;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "hardpoint.admin");
        setDescription("Remove latest capture point");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(facade.getConfigFile().getEntity().getPoints().isEmpty()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cNothing to remove.");
            return true;
        }
        facade.getConfigFile().getEntity().getPoints().remove(facade.getConfigFile().getEntity().getPoints().size() - 1);
        facade.getConfigFile().save();
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have removed the latest §3capture point");
        return true;
    }
}
