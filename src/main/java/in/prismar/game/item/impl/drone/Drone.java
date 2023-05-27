package in.prismar.game.item.impl.drone;

import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Drone {

    private Location startLocation;
    private final double maxSpeed;

    private double y;
    private DroneState state;
    private double currentSpeed;

    @Setter
    private Location destination;
    private BukkitTask task;

    private Hologram hologram;

    public Drone(Plugin plugin, Location startLocation, double y, double maxSpeed) {
        this.startLocation = startLocation;
        this.destination = startLocation;
        this.y = y;
        this.maxSpeed = maxSpeed;
        this.state = DroneState.OFFLINE;

        this.hologram = new Hologram(startLocation);
        this.hologram.addLine(HologramLineType.ITEM_HEAD, new ItemStack(Material.EMERALD_BLOCK));
        this.hologram.enable();

        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::update, 5, 5);
    }

    public void start() {
        this.state = DroneState.FLY_UP;
    }

    public void stop() {
        this.state = DroneState.FLY_DOWN;
    }

    protected void update() {
        System.out.println(this.state);
        if(this.state == DroneState.OFFLINE) {
            return;
        }
        if(this.state == DroneState.FLY_UP) {
            double yDiff = (startLocation.getY() + y) - hologram.getLocation().getY();
            System.out.println("Y diff:" + yDiff);
            if(yDiff >= 0.5) {
                hologram.move(new Vector(0, 128, 0), 0, 0, false);
            }

        }
    }


}
