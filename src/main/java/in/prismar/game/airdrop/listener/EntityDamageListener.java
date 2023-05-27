package in.prismar.game.airdrop.listener;

import in.prismar.game.airdrop.AirDrop;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageListener implements Listener {

    @Inject
    private AirDropRegistry registry;


    @EventHandler
    public void onCall(EntityDamageEvent event) {
        if (event.getEntity() instanceof ArmorStand armorStand) {
            Optional<AirDrop> optional = registry.findDropByStandLocation(armorStand.getLocation());
            if (optional.isPresent()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand armorStand) {
            Optional<AirDrop> optional = registry.findDropByStandLocation(armorStand.getLocation());
            if (optional.isPresent()) {
                event.setCancelled(true);
            }
        }
    }

}
