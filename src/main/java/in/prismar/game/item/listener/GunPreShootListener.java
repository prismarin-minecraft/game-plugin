package in.prismar.game.item.listener;

import in.prismar.game.Game;
import in.prismar.game.item.event.bus.GunPreShootEvent;
import in.prismar.library.common.event.EventSubscriber;
import lombok.AllArgsConstructor;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class GunPreShootListener implements EventSubscriber<GunPreShootEvent> {

    private final Game game;

    @Override
    public void onEvent(GunPreShootEvent event) {
        if (game.getRegionProvider().isInRegionWithFlag(event.getPlayer().getLocation(), "pvp")) {
            event.setCancelled(true);
        }
    }
}
