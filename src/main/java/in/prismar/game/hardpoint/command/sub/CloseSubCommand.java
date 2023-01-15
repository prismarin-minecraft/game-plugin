package in.prismar.game.hardpoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CloseSubCommand extends HelpSubCommand<Player> {

    private final HardpointFacade facade;

    public CloseSubCommand(HardpointFacade facade) {
        super("close");
        this.facade = facade;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "hardpoint.admin");
        setDescription("Close hardpoint");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        facade.close();
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.ARROW_RIGHT +" §6§lHARDPOINT §7is now closed!");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        for(Player current : Bukkit.getOnlinePlayers()) {
            current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.3f, 1);
        }
        return true;
    }
}
