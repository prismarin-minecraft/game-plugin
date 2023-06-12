package in.prismar.game.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.region.RegionProvider;
import in.prismar.api.warp.WarpProvider;
import in.prismar.game.Game;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerWorldChangeListener implements Listener {

    @Inject
    private Game game;

    private final WarpProvider warpProvider;

    public PlayerWorldChangeListener() {
        this.warpProvider = PrismarinApi.getProvider(WarpProvider.class);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChange(PlayerChangedWorldEvent event) {
        if(game.isCurrentlyPlayingAnyMode(event.getPlayer())) {
            if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getPlayer().setGameMode(GameMode.ADVENTURE);
            }
            return;
        }
        if(warpProvider.existsWarp("spawn")) {
            Location location = warpProvider.getWarp("spawn");
            if(location.getWorld().getName().equals(event.getPlayer().getWorld().getName())) {
                if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                    event.getPlayer().setGameMode(GameMode.ADVENTURE);
                }
            }
        }
    }
}
