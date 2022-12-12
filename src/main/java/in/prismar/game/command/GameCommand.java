package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.library.spigot.entity.GlowingEntities;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
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

        ArmorStand stand = player.getWorld().spawn(LocationUtil.getCenterOfBlock(player.getLocation().getBlock().getLocation()), ArmorStand.class);
        stand.setInvisible(true);
        stand.setRightArmPose(new EulerAngle(0, 0, 0));
        final int ticks = arguments.getInteger(0);
        double y = 0.4;
        final Particle.DustOptions options = new Particle.DustOptions(Color.RED, 1);
        Scheduler.runTimerFor(1, 1, 20 * ticks, new SchedulerRunnable() {
            @Override
            public void run() {
                if(stand.isOnGround()) {
                    if(getCurrentTicks() == 1) {
                        stand.remove();
                        return;
                    }
                    if(stand.getEquipment().getHelmet() != null) {
                        stand.getEquipment().setHelmet(null);
                    }
                    Location location = stand.getLocation().clone().add(0, 2.2, 0);
                    for (int i = 0; i < 30; i++) {
                        location = location.add(0, 0.5f, 0);

                        location.getWorld().spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1, options);
                    }
                    return;
                }
                setCurrentTicks(20 * ticks);
                Vector vector = stand.getVelocity();
                if(vector.getY() < 0) {
                    vector.setY(vector.getY() * y);
                    stand.setVelocity(vector);
                }
            }
        });
        ItemStack stack = new ItemStack(Material.CLOCK);
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(1);
        stack.setItemMeta(meta);
        stand.getEquipment().setItemInMainHand(stack);

        ItemStack parachute = new ItemStack(Material.CLOCK);
        ItemMeta parachuteMeta = stack.getItemMeta();
        parachuteMeta.setCustomModelData(2);
        parachute.setItemMeta(parachuteMeta);
        stand.getEquipment().setHelmet(parachute);

        player.sendMessage("Spawned aidrop!");
        return true;
    }


}
