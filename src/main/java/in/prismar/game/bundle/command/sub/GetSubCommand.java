package in.prismar.game.bundle.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GetSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public GetSubCommand(BundleFacade facade) {
        super("get");
        this.facade = facade;
        setDescription("Get a bundle");
        setUsage("<id>");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "bundle.admin");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis bundle does exists");
                return true;
            }
            Bundle bundle = facade.getRepository().findById(id);
            player.sendMessage(PrismarinConstants.PREFIX + "You received the " + bundle.getDisplayName() + " §7bundle");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bundlegive " + player.getName() + " " + id + " false");
            return true;
        }
        return false;
    }
}
