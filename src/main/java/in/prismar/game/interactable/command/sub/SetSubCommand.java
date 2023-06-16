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
public class SetSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public SetSubCommand(InteractableService service) {
        super("set");
        this.service = service;
        setUsage("<id> <field> <value>");
        setDescription("Set a field value of a interactable");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 4) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis interactable does not exists");
                return true;
            }
            final String fieldName = arguments.getString(2);
            Interactable interactable = service.getRepository().findById(id);
            Field found = null;
            for(Field field : interactable.getClass().getDeclaredFields()) {
                if(!field.canAccess(interactable)) {
                    field.setAccessible(true);
                }
                if(field.getName().equalsIgnoreCase(fieldName)) {
                    found = field;
                    break;
                }
            }
            if(found == null) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis field does not exists");
                return true;
            }
            Class<?> type = found.getType();
            Object value;
            if(type == String.class) {
                value = arguments.getString(3);
            } else if(type == int.class) {
                value = arguments.getInteger(3);
            } else if(type == boolean.class) {
                value = arguments.getBoolean(3);
            } else {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis field is not supported");
                return true;
            }
            try {
                found.set(interactable, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            service.getRepository().save(interactable);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have updated the field §b" + found.getName() + " §7with the value §3" + value);
            return true;
        }
        return false;
    }
}
