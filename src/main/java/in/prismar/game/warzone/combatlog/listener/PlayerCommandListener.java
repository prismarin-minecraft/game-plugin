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

import javax.inject.Inject;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerCommandListener implements Listener {

    @Inject
    private CombatLogService service;

    private final ConfigStore configStore;

    public PlayerCommandListener() {
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String[] arguments = message.split(" ");
        Player player = event.getPlayer();
        if (Bukkit.getServer().getHelpMap().getHelpTopic(arguments[0]) != null) {
            if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "combatlog.bypass") && service.isInCombatLog(player)) {
                final String[] disabledCommands = configStore.getProperty("warzone.combatlog.disabled.commands").split(",");
                for(String disabled : disabledCommands) {
                    if(message.toLowerCase().startsWith(disabled.toLowerCase())) {
                        event.setCancelled(true);
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou cannot execute this command while being in combat");
                        return;
                    }
                }
            }

        }
    }
}
