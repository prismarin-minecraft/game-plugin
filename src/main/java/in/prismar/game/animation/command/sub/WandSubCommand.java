package in.prismar.game.animation.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.animation.AnimationFacade;
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
public class WandSubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public WandSubCommand(AnimationFacade facade) {
        super("wand");
        this.facade = facade;
        setDescription("Get the wand item");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have received the §aWand §7item");
        player.getInventory().addItem(AnimationFacade.WAND_ITEM);
        return true;
    }
}
