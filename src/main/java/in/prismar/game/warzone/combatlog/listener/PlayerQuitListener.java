package in.prismar.game.warzone.combatlog.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.warzone.combatlog.CombatLogService;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerQuitListener implements Listener {

    @Inject
    private CombatLogService service;

    private final ConfigStore configStore;

    public PlayerQuitListener() {
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "combatlog.bypass") && service.isInCombatLog(player)) {
            player.setHealth(0);
        }
    }
}
