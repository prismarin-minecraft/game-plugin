package in.prismar.game.perk.listener;

import in.prismar.game.perk.PerkService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
    private PerkService service;

    @EventHandler
    public void onDamage(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        service.giveDefaultPerk(player);
    }
}
