package in.prismar.game.item.listener.custom;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class CustomItemFishListener implements Listener {

    @Inject
    private CustomItemRegistry registry;

    @Inject
    private Game game;

    @EventHandler
    public void onCall(PlayerFishEvent event) {
        registry.publishEvent(event.getPlayer(), event);
    }
}
