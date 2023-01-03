package in.prismar.game.ffa.listener.player;

import in.prismar.game.ffa.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerDropItemListener implements Listener {

    @Inject
    private GameMapFacade facade;

    @EventHandler
    public void onCall(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            event.setCancelled(true);
        }
    }
}
