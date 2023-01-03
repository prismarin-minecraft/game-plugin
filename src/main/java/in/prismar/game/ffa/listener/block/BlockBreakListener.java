package in.prismar.game.ffa.listener.block;

import in.prismar.game.ffa.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class BlockBreakListener implements Listener {

    @Inject
    private GameMapFacade facade;

    @EventHandler
    public void onCall(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            event.setCancelled(true);
        }
    }
}
