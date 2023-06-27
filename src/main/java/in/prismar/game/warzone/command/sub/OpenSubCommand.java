package in.prismar.game.warzone.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.warzone.WarzoneService;
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

    private final WarzoneService service;

    public OpenSubCommand(WarzoneService service) {
        super("open");
        this.service = service;
        setDescription("Open warzone");

        setPermission(PrismarinConstants.PERMISSION_PREFIX + "warzone.admin");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        service.open();
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.ARROW_RIGHT +" §c§lWarzone §7is now open!");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        for(Player current : Bukkit.getOnlinePlayers()) {
            current.playSound(current.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.3f, 1);
        }
        return true;
    }
}
