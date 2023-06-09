package in.prismar.game.item.listener.custom;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class CustomItemSlotChangeListener implements Listener {

    @Inject
    private CustomItemRegistry registry;

    @Inject
    private Game game;

    @EventHandler
    public void onCall(PlayerItemHeldEvent event) {
        registry.scan(event.getPlayer());
        registry.publishEvent(event.getPlayer(), event);
    }
}
