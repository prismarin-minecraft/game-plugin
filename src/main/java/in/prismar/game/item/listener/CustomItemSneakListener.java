package in.prismar.game.item.listener;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class CustomItemSneakListener implements Listener {

    @Inject
    private CustomItemRegistry registry;

    @Inject
    private Game game;

    @EventHandler
    public void onCall(PlayerToggleSneakEvent event) {
        registry.publishEvent(event.getPlayer(), event);
    }
}
