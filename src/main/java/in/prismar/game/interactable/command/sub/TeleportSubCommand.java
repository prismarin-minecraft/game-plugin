package in.prismar.game.interactable.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class TeleportSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public TeleportSubCommand(InteractableService service) {
        super("teleport");
        this.service = service;
        setUsage("<id>");
        setDescription("Teleport to a interactable");
        setAliases("tp");
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
            player.teleport(interactable.getLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1f);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have been teleported to §b" + id);
            return true;
        }
        return false;
    }
}
