package in.prismar.game.ffa.arsenal.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
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
    private GameMapFacade facade;

    private final UserProvider<User> userProvider;

    public PlayerJoinListener() {
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCall(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(user.getSeasonData().getArsenal().isEmpty()) {
            facade.getArsenalService().giveStarterArsenal(user);
        }
    }
}
