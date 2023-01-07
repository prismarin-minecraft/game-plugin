package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.library.spigot.entity.GlowingEntities;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.location.LocationUtil;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class GameCommand extends SpigotCommand<Player> {

    public GameCommand(Game game) {
        super("game");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "game");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        Frame frame = new Frame("§f七七七七七七七七ㇺ", 6);
        frame.addButton(25, new ItemStack(Material.STICK));
        frame.build();
        frame.openInventory(player);
        return true;
    }


}
