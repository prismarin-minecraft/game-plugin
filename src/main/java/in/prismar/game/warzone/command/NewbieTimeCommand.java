package in.prismar.game.warzone.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class NewbieTimeCommand extends PlayerCommand {

    @Inject
    private WarzoneService service;

    public NewbieTimeCommand() {
        super("newbietime");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 1 && player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "newbie.other")) {
            Player target = arguments.getOnlinePlayer(0);
            if(!service.isOnNewbieProtection(target)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis player does not have a newbie protection enabled");
                return true;
            }
            User user = service.getUserProvider().getUserByUUID(target.getUniqueId());
            long newbieTime = user.getTimestamp("newbie.protection") - System.currentTimeMillis();
            player.sendMessage(PrismarinConstants.PREFIX + "§7Newbie protection time of §3" + target.getName() + "§8: §a" + TimeUtil.convertToTwoDigits(newbieTime / 1000));
            return true;
        }
        if(!service.isOnNewbieProtection(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYour newbie protection already expired");
            return true;
        }
        User user = service.getUserProvider().getUserByUUID(player.getUniqueId());
        long newbieTime = user.getTimestamp("newbie.protection") - System.currentTimeMillis();
        player.sendMessage(PrismarinConstants.PREFIX + "§7Newbie protection time of §3" + player.getName() + "§8: §a" + TimeUtil.convertToTwoDigits(newbieTime / 1000));
        return true;
    }
}
