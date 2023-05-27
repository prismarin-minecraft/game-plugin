package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class WorldChangeListener implements Listener {

    @Inject
    private WarzoneService warzoneService;

    private final ScoreboardProvider scoreboardProvider;

    public WorldChangeListener() {
        this.scoreboardProvider = PrismarinApi.getProvider(ScoreboardProvider.class);
    }

    @EventHandler
    public void onChange(PlayerChangedWorldEvent event) {
        Location location = warzoneService.getWarzoneLocation();
        Player player = event.getPlayer();
        if(location != null) {
            if(event.getFrom().getName().equals(location.getWorld().getName())) {
                scoreboardProvider.recreateTablist(player);
                return;
            }
            if(warzoneService.isInWarzone(player)) {
                scoreboardProvider.recreateTablist(player);
            }
        }

    }
}
