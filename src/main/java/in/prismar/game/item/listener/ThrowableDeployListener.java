package in.prismar.game.item.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.item.event.bus.ThrowableDeployEvent;
import in.prismar.library.common.event.EventSubscriber;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class ThrowableDeployListener implements EventSubscriber<ThrowableDeployEvent> {

    private final Game game;

    @Override
    public void onEvent(ThrowableDeployEvent event) {
        Player player = event.getPlayer();
        if (game.getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp")) {
            event.setCancelled(true);
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not allowed to use this item inside a safe region.");
            return;
        }
    }
}