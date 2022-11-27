package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class GameCommand extends SpigotCommand<Player> {


    public GameCommand() {
        super("game");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "game");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        for(Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5)) {
            player.sendMessage("Nearby: " + entity.getType().name());
        }
        player.setHealth(0);
        return true;
    }


}
