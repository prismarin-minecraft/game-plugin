package in.prismar.game.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.map.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerCommandListener implements Listener {

    private ConfigStore configStore;

    @Inject
    private GameMapFacade mapFacade;

    @Inject
    private ExtractionFacade extractionFacade;

    public PlayerCommandListener() {
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        String[] arguments = message.split(" ");
        Player player = event.getPlayer();
        if (Bukkit.getServer().getHelpMap().getHelpTopic(arguments[0]) != null) {
            if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "disabledcommands.bypass")) {
                final String[] disabledCommands;
                if(extractionFacade.isIn(player) && !extractionFacade.isInSafeZone(player)) {
                    disabledCommands = configStore.getProperty("extraction.disabled.commands").split(",");
                } else if(mapFacade.isInMap(player.getUniqueId())) {
                    disabledCommands = configStore.getProperty("ffa.disabled.commands").split(",");
                } else {
                    disabledCommands = new String[0];
                }
                for(String disabled : disabledCommands) {
                    if(message.toLowerCase().startsWith(disabled.toLowerCase())) {
                        event.setCancelled(true);
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou cannot use this command ingame.");
                        return;
                    }
                }
            }

        }
    }
}
