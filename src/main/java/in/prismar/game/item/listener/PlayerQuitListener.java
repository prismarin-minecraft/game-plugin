package in.prismar.game.item.listener;

import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.player.GunPlayer;
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
    private CustomItemRegistry registry;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GunPlayer.remove(player);
        registry.getHolders().remove(player);
    }
}
