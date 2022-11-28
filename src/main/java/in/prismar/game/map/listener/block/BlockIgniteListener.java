package in.prismar.game.map.listener.block;

import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class BlockIgniteListener implements Listener {

    @EventHandler
    public void onCall(BlockIgniteEvent event) {
        event.setCancelled(true);
    }
}
