package in.prismar.game.ffa.listener.player;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInventoryClickListener implements Listener {

    @Inject
    private GameMapFacade facade;

    @EventHandler
    public void onCall(InventoryClickEvent event) {
        if(event.getClickedInventory() != null) {
            Player player = (Player) event.getWhoClicked();
            if(facade.isCurrentlyPlaying(player) && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "inventory.click.bypass")) {
                event.setCancelled(true);
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
