package in.prismar.game.ffa.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class ProtectionListener implements Listener {

    @Inject
    private GameMapFacade facade;

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            event.setCancelled(true);
        }
    }



    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory() != null) {
            Player player = (Player) event.getWhoClicked();
            if(facade.isCurrentlyPlaying(player) && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "inventory.click.bypass")) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(facade.isCurrentlyPlaying(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(event.getClickedBlock() != null) {
                if(facade.isCurrentlyPlaying(event.getPlayer())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof ArmorStand) {
            if(event.getDamager() instanceof Player damager) {
                if(facade.isCurrentlyPlaying(damager)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityBreak(HangingBreakByEntityEvent event) {
        if(event.getRemover() instanceof Player remover) {
            if(facade.isCurrentlyPlaying(remover)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if(facade.isCurrentlyPlaying(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        if(facade.isCurrentlyPlaying(event.getPlayer())) {
            event.setCancelled(true);
        }
    }
}
