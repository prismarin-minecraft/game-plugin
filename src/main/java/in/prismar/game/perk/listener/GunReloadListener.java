package in.prismar.game.perk.listener;

import in.prismar.game.item.event.bus.GunReloadEvent;
import in.prismar.game.perk.Perk;
import in.prismar.game.perk.PerkService;
import in.prismar.library.common.event.EventSubscriber;
import lombok.AllArgsConstructor;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class GunReloadListener implements EventSubscriber<GunReloadEvent> {

    private final PerkService service;

    @Override
    public void onEvent(GunReloadEvent event) {
        if(service.hasPerkAndAllowedToUse(event.getPlayer(), Perk.FASTHANDS)) {
            event.setReloadTimeInTicks(Math.round(event.getReloadTimeInTicks() / 2));
        }
    }
}
