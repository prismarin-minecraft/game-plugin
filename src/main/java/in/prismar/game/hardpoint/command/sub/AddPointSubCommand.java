package in.prismar.game.hardpoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.game.hardpoint.config.Hardpoint;
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
public class AddPointSubCommand extends HelpSubCommand<Player> {

    private final HardpointFacade facade;

    public AddPointSubCommand(HardpointFacade facade) {
        super("addpoint");
        this.facade = facade;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "hardpoint.admin");
        setDescription("Add a capture point");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        Hardpoint hardpoint = new Hardpoint(player.getLocation());
        hardpoint.setCircle(facade.getConfigFile().generateCircle(player.getLocation()));
        facade.getConfigFile().getEntity().getPoints().add(hardpoint);
        facade.getConfigFile().save();
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have added a new §bcapture point");
        return true;
    }
}
