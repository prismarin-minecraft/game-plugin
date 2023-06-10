package in.prismar.game.warzone.combatlog.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.game.warzone.combatlog.CombatLogService;
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
public class PlayerWorldChangeListener implements Listener {

    @Inject
    private WarzoneService warzoneService;

    @Inject
    private CombatLogService combatLogService;


    @EventHandler
    public void onChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        if(!warzoneService.isInWarzone(player)) {
            combatLogService.removeCombatLog(player);
        }

    }
}
