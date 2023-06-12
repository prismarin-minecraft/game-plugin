package in.prismar.game.item.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.item.event.spigot.ThrowableDeployEvent;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    @EventHandler
    public void onEvent(ThrowableDeployEvent event) {
        Player player = event.getPlayer();
        if (game.getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp")) {
            event.setCancelled(true);
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not allowed to use this item inside a safe region.");
        }
    }
}
