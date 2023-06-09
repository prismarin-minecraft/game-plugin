package in.prismar.game.item.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.item.event.bus.ThrowableDeployEvent;
import in.prismar.game.item.event.bus.ThrowableExplodeEvent;
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
public class ThrowableExplodeListener implements EventSubscriber<ThrowableExplodeEvent> {

    private final Game game;

    @Override
    public void onEvent(ThrowableExplodeEvent event) {
        if (game.getRegionProvider().isInRegionWithFlag(event.getLocation(), "pvp")) {
            event.setCancelled(true);
        }
    }
}
