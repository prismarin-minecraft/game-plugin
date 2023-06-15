package in.prismar.game.interactable.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
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
public class ListSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public ListSubCommand(InteractableService service) {
        super("list");
        this.service = service;
        setDescription("List all interactables");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7All interactables§8: §b" + Joiner.on("§8, §b").join(service.getRepository().findAll()));
        return true;
    }
}
