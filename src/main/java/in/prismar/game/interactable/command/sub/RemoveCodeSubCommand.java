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
public class RemoveCodeSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public RemoveCodeSubCommand(InteractableService service) {
        super("removecode");
        this.service = service;
        setUsage("<id> <code>");
        setDescription("Remove code from keycode lock");
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
            final String code = arguments.getString(2);
            Keycode keycode = (Keycode)interactable;
            if(!keycode.getCodes().contains(code)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis code does not exists");
                return true;
            }
            keycode.getCodes().remove(code);
            service.getRepository().save(keycode);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have removed the code §3" + code);
            return true;
        }
        return false;
    }
}
