package in.prismar.game.warzone.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
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

    private final UserProvider<User> userProvider;
    private final ConfigStore configStore;

    public ThrowableDeployListener() {
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEvent(ThrowableDeployEvent event) {
        if(event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (game.getRegionProvider().isIn(player.getLocation(), "warzone")) {
            User user = userProvider.getUserByUUID(player.getUniqueId());
            if(!user.isLocalTimestampAvailable("throwable.cooldown") && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "throwable.warzone.bypass")) {
                long time = user.getLocalTimestamp("throwable.cooldown") - System.currentTimeMillis();
                player.sendMessage(PrismarinConstants.PREFIX + "§cThrowing is on cooldown. §8(§c" + TimeUtil.convertToTwoDigits(time / 1000) + "§8)");
                event.setCancelled(true);
                return;
            }
            long cooldown = Long.parseLong(configStore.getProperty("throwable.cooldown"));
            user.setLocalTimestamp("throwable.cooldown", System.currentTimeMillis() + cooldown);
            return;
        }
    }
}
