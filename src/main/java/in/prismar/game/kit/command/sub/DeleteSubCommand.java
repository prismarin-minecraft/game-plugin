package in.prismar.game.kit.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.kit.KitService;
import in.prismar.game.kit.model.Kit;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DeleteSubCommand extends HelpSubCommand<Player> {

    private final KitService service;

    public DeleteSubCommand(KitService service) {
        super("delete");
        this.service = service;
        setUsage("<id>");
        setDescription("Delete a kit");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis kit does not exists");
                return true;
            }
            Kit kit = service.getRepository().findById(id);
            service.getRepository().delete(kit);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully deleted the kit §a" + id);
            return true;
        }
        return false;
    }
}
