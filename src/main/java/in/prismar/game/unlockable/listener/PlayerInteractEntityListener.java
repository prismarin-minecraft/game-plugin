package in.prismar.game.unlockable.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.unlockable.Unlockable;
import in.prismar.game.unlockable.UnlockableService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
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
    private UnlockableService service;

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getHand() == EquipmentSlot.HAND) {
            Entity entity = event.getRightClicked();
            if (entity.getType() == EntityType.ARMOR_STAND) {
                for(Unlockable unlockable : service.getFile().getEntity().values()) {
                    Location location = unlockable.getLocation();
                    if(!entity.getLocation().getWorld().getName().equals(location.getWorld().getName())) {
                        continue;
                    }
                    if(entity.getLocation().distanceSquared(location) <= 5) {
                        Player player = event.getPlayer();
                        if(player.isSneaking() && player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "unlockable.modify")) {
                            event.setCancelled(false);
                            return;
                        }
                        service.unlock(player, unlockable);
                        event.setCancelled(true);
                        return;
                    }
                }

            }
        }
    }
}
