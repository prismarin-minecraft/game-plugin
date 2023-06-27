package in.prismar.game.kit.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.kit.KitService;
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
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final KitService service;

    public CreateSubCommand(KitService service) {
        super("create");
        this.service = service;
        setUsage("<id> <cooldownInSeconds>");
        setDescription("Create a new kit");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis kit already exists");
                return true;
            }
            if(!ItemUtil.hasItemInHandAndHasDisplayName(player, true)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold an item with a displayname in your main hand");
                return true;
            }
            final int cooldown = arguments.getInteger(2);
            final ItemStack stack = player.getInventory().getItemInMainHand().clone();
            service.create(id, stack, cooldown);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully created the kit §a" + id);
            return true;
        }
        return false;
    }
}
