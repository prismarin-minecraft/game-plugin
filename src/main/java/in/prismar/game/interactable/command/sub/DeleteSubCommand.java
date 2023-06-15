package in.prismar.game.interactable.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.InteractableType;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DeleteSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public DeleteSubCommand(InteractableService service) {
        super("delete");
        this.service = service;
        setUsage("<id>");
        setDescription("Delete a interactable");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis interactable does not exists");
                return true;
            }
            service.getRepository().deleteById(id);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have deleted §3" + id);
            return true;
        }
        return false;
    }
}
