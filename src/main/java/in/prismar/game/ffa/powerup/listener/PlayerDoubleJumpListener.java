package in.prismar.game.ffa.powerup.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerDoubleJumpListener implements Listener {

    @Inject
    private FFAFacade facade;

    private final UserProvider<User> provider;

    public PlayerDoubleJumpListener() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
    }

    @EventHandler
    public void onFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            User user = provider.getUserByUUID(player.getUniqueId());
            if(user.containsTag("doubleJump")) {
                long time = user.getTag("doubleJump");
                if(System.currentTimeMillis() <= time) {
                    if(player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.setVelocity(player.getLocation().getDirection().multiply(1).setY(0.9));
                        player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.7f, 1);
                    }
                } else {
                    user.removeTag("doubleJump");
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }

            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(facade.isCurrentlyPlaying(player)) {
            if(player.isOnGround()) {
                User user = provider.getUserByUUID(player.getUniqueId());
                if(user.containsTag("doubleJump")) {
                    long time = user.getTag("doubleJump");
                    if(System.currentTimeMillis() <= time) {
                        player.setFlying(false);
                        player.setAllowFlight(true);
                    }
                }
            }
        }
    }
}
