package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.Game;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
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

    @Inject
    private GameStatsDistributor statsDistributor;



    @EventHandler(priority = EventPriority.HIGH)
    public void onCall(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(service.isInWarzone(player)) {
            event.setDeathMessage(null);
            if(isWorth(event.getDrops())) {
                service.createTombstone(player, event.getDrops());
                event.getDrops().clear();
            }
            if(event.getEntity().getKiller() != null) {
                Player killer = event.getEntity().getKiller();
                statsDistributor.addKill(killer);
                statsDistributor.addWarzoneKill(killer);
            }
            statsDistributor.addWarzoneDeath(player);
            statsDistributor.addDeath(player);
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
        return worth >= 6;
    }


}
