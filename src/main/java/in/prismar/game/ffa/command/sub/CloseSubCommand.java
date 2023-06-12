package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.ffa.repository.FFAMapRepository;
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

    private final FFAFacade facade;
    private final FFAMapRepository repository;

    public CloseSubCommand(FFAFacade facade) {
        super("close");
        setDescription("Close ffa");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "ffa.close");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        facade.close();
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.ARROW_RIGHT +" §b§lFFA §7is now closed!");
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(PrismarinConstants.BORDER);
        for(Player current : Bukkit.getOnlinePlayers()) {
            current.playSound(current.getLocation(), Sound.ENTITY_CAT_HISS, 0.3f, 1);
        }
        return true;
    }
}
