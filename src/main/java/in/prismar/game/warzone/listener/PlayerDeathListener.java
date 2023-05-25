package in.prismar.game.warzone.listener;

import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerDeathListener implements Listener {

    @Inject
    private WarzoneService service;

    @EventHandler(priority = EventPriority.HIGH)
    public void onCall(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(service.isInWarzone(player)) {
            if(isWorth(event.getDrops())) {
                service.createTombstone(player, event.getDrops());
                event.getDrops().clear();
            }
        }
    }

    private boolean isWorth(List<ItemStack> items) {
        int worth = 0;
        for(ItemStack stack : items) {
            if(stack != null) {
                if(stack.getType() != Material.AIR) {
                    worth++;
                }
            }
        }
        return worth >= 3;
    }
}
