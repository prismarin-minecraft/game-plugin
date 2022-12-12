package in.prismar.game.bundle.command;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class BundleGiveCommand extends SpigotCommand<CommandSender> {

    @Inject
    private BundleFacade facade;

    public BundleGiveCommand() {
        super("bundlegive");
        setSenders(ConsoleCommandSender.class);
    }

    @Override
    public boolean send(CommandSender sender, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            Player target = arguments.getOnlinePlayer(0);
            final String id = arguments.getString(1);
            if(!facade.getRepository().existsById(id)) {
                sender.sendMessage("Bundle " + id + " does not exists");
                return true;
            }
            Bundle bundle = facade.getRepository().findById(id);
            if(bundle.getContainer() == null) {
                sender.sendMessage("Bundle " + id + " is empty");
                return true;
            }
            for(ItemStack stack : bundle.getContainer().getItem()) {
                if(stack != null) {
                    if(stack.getType() != Material.AIR) {
                        ItemUtil.giveItem(target, stack);
                    }
                }
            }
            target.sendMessage(" ");
            target.sendMessage(PrismarinConstants.PREFIX + "§7You received the §b" + bundle.getDisplayName() + " §7bundle.");
            target.sendMessage(" ");
            target.playSound(target.getLocation(), Sound.ITEM_TOTEM_USE, 0.6f, 1);
            sender.sendMessage("Player " + target.getName() + " received the bundle " + bundle.getId());
            return true;
        }
        return false;
    }
}
