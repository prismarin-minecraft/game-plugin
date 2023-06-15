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
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final InteractableService service;
    public CreateSubCommand(InteractableService service) {
        super("create");
        this.service = service;
        setUsage("<id> <type>");
        setDescription("Create a new interactable");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis id is already taken");
                return true;
            }
            final InteractableType type = arguments.getEnumType(InteractableType.values(), 2);
            RayTraceResult result = player.getWorld().rayTraceEntities(player.getEyeLocation(), player.getLocation().getDirection().normalize(), 10, entity -> entity.getType() == EntityType.ITEM_FRAME);
            if(result.getHitEntity() == null) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease look at an item frame.");
                return true;
            }
            service.create(id, result.getHitEntity().getLocation(), type);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully created §b" + id);
            return true;
        }
        return false;
    }
}
