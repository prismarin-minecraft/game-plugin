package in.prismar.game.item;

import in.prismar.game.Game;
import in.prismar.game.item.holder.CustomItemHolder;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CustomItemUpdater implements Runnable, Listener {

    private Game game;
    private CustomItemRegistry registry;

    private Set<Player> scanQueue = new HashSet<>();

    public CustomItemUpdater(Game game, CustomItemRegistry registry) {
        this.game = game;
        this.registry = registry;
        Bukkit.getPluginManager().registerEvents(this, game);
    }

    @Override
    public void run() {
        for(Player player : scanQueue) {
            registry.scan(player);
        }
        scanQueue.clear();
        for (Map.Entry<Player, List<CustomItemHolder>> entry : registry.getHolders().entrySet()) {
            for (CustomItemHolder holder : entry.getValue()) {
                holder.getItem().getEventBus().publish(entry.getKey(), game, holder, holder);
            }
        }
    }

    @EventHandler
    public void onChangeSlot(PlayerItemHeldEvent event) {
        scanQueue.add(event.getPlayer());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        scanQueue.add((Player) event.getWhoClicked());
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        scanQueue.add((Player) event.getWhoClicked());
    }


    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            scanQueue.add(player);
        }
    }

    @EventHandler
    public void onDrop(EntityDropItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            scanQueue.add(player);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        scanQueue.add(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        scanQueue.add(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        scanQueue.remove(event.getPlayer());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        scanQueue.add(event.getPlayer());
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        scanQueue.add(event.getPlayer());
    }

    @EventHandler
    public void onSwap(PlayerInteractEvent event) {
        scanQueue.add(event.getPlayer());
    }
}
