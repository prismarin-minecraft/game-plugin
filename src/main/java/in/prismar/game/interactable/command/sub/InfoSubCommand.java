package in.prismar.game.interactable.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class InfoSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public InfoSubCommand(InteractableService service) {
        super("info");
        this.service = service;
        setUsage("<id>");
        setDescription("Info about a interactable");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis interactable does not exists");
                return true;
            }
            Interactable interactable = service.getRepository().findById(id);

            player.sendMessage(PrismarinConstants.BORDER);
            player.sendMessage(" ");
            player.sendMessage(PrismarinConstants.ARROW_RIGHT + " §7ID§8: §b" + interactable.getId());
            player.sendMessage(PrismarinConstants.ARROW_RIGHT + " §7Type§8: §b" + interactable.getClass().getSimpleName());
            for(Field field : interactable.getClass().getDeclaredFields()) {
                if(!field.canAccess(interactable)) {
                    field.setAccessible(true);
                }
                Class<?> type = field.getType();
                if(type == String.class || type == int.class || type == boolean.class) {
                    try {
                        player.sendMessage("  " + PrismarinConstants.LISTING_DOT + " §7" + field.getName() + "§8: §b" + field.get(interactable));
                    } catch (IllegalAccessException e) {}
                }
            }
            player.sendMessage(" ");
            player.sendMessage(PrismarinConstants.BORDER);
            return true;
        }
        return false;
    }
}
