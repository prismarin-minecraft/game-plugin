package in.prismar.game.item.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.meta.Provider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.spigot.ThrowableDeployEvent;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@AutoListener
public class ThrowableDeployListener implements Listener {

    @Inject
    private Game game;

    @Provider
    private UserProvider<User> userProvider;

    @Provider
    private ConfigStore configStore;

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEvent(ThrowableDeployEvent event) {
        Player player = event.getPlayer();
        if (game.getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp")) {
            event.setCancelled(true);
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not allowed to use this item inside a safe region.");
            return;
        }
        if(game.getWarzoneService().isInWarzone(player)) {
            User user = userProvider.getUserByUUID(player.getUniqueId());
            if(!user.isLocalTimestampAvailable("throwable")) {
                event.setCancelled(true);
                long difference = user.getLocalTimestamp("throwable") - System.currentTimeMillis();
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou must wait another "+ TimeUtil.showInHoursMinutesSeconds(difference/1000) + " before throwing another throwable");
                return;
            }
            user.setLocalTimestamp("throwable", System.currentTimeMillis() + Long.valueOf(configStore.getProperty("warzone.throwable.cooldown")));
            return;
        }

    }
}
