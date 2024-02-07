package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerJoinListener implements Listener {

    @Inject
    private WarzoneService warzoneService;


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(warzoneService.isInWarzone(player)) {
            if(warzoneService.tryEnableNewbieProtection(player)) {
                long time = warzoneService.getNewbieProtectionTime();
                player.sendMessage(PrismarinConstants.PREFIX + "§7Your §anewbie protection §7has been enabled. §8(§7Duration§8: §a" + TimeUtil.convertToTwoDigits(time/1000) + "§8)");
            }
            player.setGameMode(GameMode.ADVENTURE);
        }
    }
}
