package in.prismar.game.perk.listener;

import in.prismar.game.item.event.spigot.GunReloadEvent;
import in.prismar.game.perk.Perk;
import in.prismar.game.perk.PerkService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class GunReloadListener implements Listener {

    @Inject
    private PerkService service;

    @EventHandler
    public void onEvent(GunReloadEvent event) {
        if(service.hasPerkAndAllowedToUse(event.getPlayer(), Perk.FASTHANDS)) {
            event.setReloadTimeInTicks(Math.round(event.getReloadTimeInTicks() / 2));
        }
    }
}
