package in.prismar.game.keycode;

import in.prismar.api.PrismarinApi;
import in.prismar.api.location.LocationProvider;
import in.prismar.library.spigot.entity.EntityInteracter;
import in.prismar.library.spigot.entity.EntityUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class KeyCode implements Listener {

    private Location location;
    private KeyCodeFrame.KeyCodeCallback callback;

    public KeyCode(Plugin plugin, String key, KeyCodeFrame.KeyCodeCallback callback) {
        LocationProvider locationProvider = PrismarinApi.getProvider(LocationProvider.class);
        Optional<Location> optional = locationProvider.getLocationOptional(key);
        if(optional.isPresent()) {
            this.location = optional.get();
            this.callback = callback;

            Bukkit.getPluginManager().registerEvents(this, plugin);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if(location == null) {
            return;
        }
        if (event.getHand() == EquipmentSlot.HAND) {
            Entity entity = event.getRightClicked();
            if (entity.getType() == EntityType.ITEM_FRAME) {
                if(entity.getLocation().distanceSquared(location) <= 2) {
                    KeyCodeFrame frame = new KeyCodeFrame(callback);
                    frame.openInventory(event.getPlayer());
                    event.setCancelled(true);
                }
            }
        }
    }
}
