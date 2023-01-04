package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.ffa.repository.GameMapRepository;
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
public class JoinSubCommand extends HelpSubCommand<Player> {

    private final GameMapFacade facade;
    private final GameMapRepository repository;

    public JoinSubCommand(GameMapFacade facade) {
        super("join");
        setDescription("Join the current map");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (facade.getRotator().getCurrentMap() == null) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cNo map is currently open");
            return true;
        }
        if(facade.getRotator().getCurrentMap().getPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already playing");
            return true;
        }
        if(!facade.isOpen() && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX +"ffa.join.bypass")) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cFFA is closed!");
            return true;
        }
        facade.join(player);
        player.sendMessage(PrismarinConstants.PREFIX + "§7You are now playing on §b" +
                facade.getRotator().getCurrentMap().getIcon().getItem().getItemMeta().getDisplayName());
        return true;
    }
}
