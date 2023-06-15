package in.prismar.game.interactable.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.interactable.model.keycode.KeyCodeFrame;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractEntityListener implements Listener {

    @Inject
    private InteractableService service;

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Entity entity = event.getRightClicked();
            if (entity.getType() == EntityType.ITEM_FRAME) {
                for(Interactable interactable : service.getRepository().findAll()) {
                    Location location = interactable.getLocation();
                    if(entity.getLocation().distanceSquared(location) <= 2) {
                        Player player = event.getPlayer();
                        if(player.isSneaking() && player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "interactable.modify")) {
                            event.setCancelled(false);
                            return;
                        }
                        interactable.onInteract(service, player);
                        event.setCancelled(true);
                    }
                }

            }
        }
    }
}
