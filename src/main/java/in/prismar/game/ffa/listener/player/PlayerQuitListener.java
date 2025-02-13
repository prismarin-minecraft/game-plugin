package in.prismar.game.ffa.listener.player;

import in.prismar.game.ffa.FFAFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerQuitListener implements Listener {

    @Inject
    private FFAFacade facade;

    @EventHandler
    public void onCall(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            facade.leave(player);
        }
    }
}
