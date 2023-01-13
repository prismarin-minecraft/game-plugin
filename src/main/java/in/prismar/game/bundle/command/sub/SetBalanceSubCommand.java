package in.prismar.game.bundle.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
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
public class SetBalanceSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public SetBalanceSubCommand(BundleFacade facade) {
        super("setbalance");
        this.facade = facade;
        setDescription("Set balance of a bundle");
        setUsage("<id> <balance>");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(!facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis bundle does exists");
                return true;
            }
            final double balance = arguments.getDouble(2);
            Bundle bundle = facade.getRepository().findById(id);
            bundle.setBalance(balance);
            facade.getRepository().save(bundle);
            player.sendMessage(PrismarinConstants.PREFIX + "You have updated the bundle §b" + id);
            return true;
        }
        return false;
    }
}
