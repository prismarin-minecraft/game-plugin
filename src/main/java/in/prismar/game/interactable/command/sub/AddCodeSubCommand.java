package in.prismar.game.interactable.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.interactable.model.keycode.Keycode;
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
public class AddCodeSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public AddCodeSubCommand(InteractableService service) {
        super("addcode");
        this.service = service;
        setUsage("<id> <code>");
        setDescription("Add code to a keycode");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis interactable does not exists");
                return true;
            }
            Interactable interactable = service.getRepository().findById(id);
            if(interactable.getClass() != Keycode.class) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou can only add a code to a keycode lock");
                return true;
            }
            Keycode keycode = (Keycode)interactable;
            keycode.getCodes().add(arguments.getString(2));
            service.getRepository().save(keycode);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have added a new §bcode");
            return true;
        }
        return false;
    }
}
