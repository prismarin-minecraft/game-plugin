package in.prismar.game.bundle.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.location.LocationProvider;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractEntityListener implements Listener {

    private LocationProvider locationProvider;

    public PlayerInteractEntityListener() {
        this.locationProvider = PrismarinApi.getProvider(LocationProvider.class);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Entity entity = event.getRightClicked();
            if (entity.getType() == EntityType.ARMOR_STAND) {
                if (locationProvider.existsLocation("bundle.1")) {
                    Location location = locationProvider.getLocation("bundle.1");
                    if (entity.getLocation().getWorld().getName().equals(location.getWorld().getName())) {
                        if (entity.getLocation().distanceSquared(location) <= 5) {
                            event.setCancelled(true);
                            event.getPlayer().performCommand("bundles");
                            return;
                        }
                    }
                }
                if (locationProvider.existsLocation("bundle.2")) {
                    Location location = locationProvider.getLocation("bundle.2");
                    if (entity.getLocation().getWorld().getName().equals(location.getWorld().getName())) {
                        if (entity.getLocation().distanceSquared(location) <= 5) {
                            event.setCancelled(true);
                            event.getPlayer().performCommand("bundles");
                            return;
                        }
                    }
                }
            }
        }
    }
}
