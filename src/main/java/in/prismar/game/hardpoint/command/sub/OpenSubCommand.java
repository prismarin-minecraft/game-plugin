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
public class OpenSubCommand extends HelpSubCommand<Player> {

    private final HardpointFacade facade;

    public OpenSubCommand(HardpointFacade facade) {
        super("open");
        this.facade = facade;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "hardpoint.admin");
        setDescription("Open hardpoint");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        facade.open();
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.ARROW_RIGHT +" §6§lHARDPOINT §7is now open!");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        for(Player current : Bukkit.getOnlinePlayers()) {
            current.playSound(current.getLocation(), Sound.ENTITY_CAT_HISS, 0.3f, 1);
        }
        return true;
    }
}
