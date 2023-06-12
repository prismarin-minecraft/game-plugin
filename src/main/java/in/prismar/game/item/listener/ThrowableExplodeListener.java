package in.prismar.game.item.listener;

import in.prismar.game.Game;
import in.prismar.game.item.event.spigot.ThrowableExplodeEvent;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class ThrowableExplodeListener implements Listener {

    @Inject
    private Game game;

    @EventHandler
    public void onEvent(ThrowableExplodeEvent event) {
        if (game.getRegionProvider().isInRegionWithFlag(event.getLocation(), "pvp")) {
            event.setCancelled(true);
        }
    }
}
