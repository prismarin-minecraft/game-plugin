package in.prismar.game.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.warzone.WarzoneService;
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

    private final ConfigStore configStore;

    @Inject
    private FFAFacade mapFacade;

   /* @Inject
    private ExtractionFacade extractionFacade;*/

    @Inject
    private HardpointFacade hardpointFacade;

    @Inject
    private WarzoneService warzoneService;

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
                if(mapFacade.isInMap(player.getUniqueId()) || hardpointFacade.isCurrentlyPlaying(player)) {
                    disabledCommands = configStore.getProperty("game.disabled.commands").split(",");
                } else if(warzoneService.isInWarzone(player) && warzoneService.isInSafeZone(player)) {
                    disabledCommands = configStore.getProperty("warzone.disabled.commands").split(",");
                } else {
                    disabledCommands = new String[0];
                }
                for(String disabled : disabledCommands) {
                    if(message.toLowerCase().startsWith(disabled.toLowerCase())) {
                        event.setCancelled(true);
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou cannot use this command here");
                        return;
                    }
                }
            }

        }
    }
}
