package in.prismar.game.warzone.combatlog.listener;

import in.prismar.game.Game;
import in.prismar.game.warzone.combatlog.npc.TemporaryNpc;
import in.prismar.game.warzone.combatlog.npc.TemporaryNpcService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
    private TemporaryNpcService temporaryNpcService;

    @Inject
    private Game game;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(temporaryNpcService.hasNpc(player.getUniqueId())) {
            TemporaryNpc npc = temporaryNpcService.getNpc(player.getUniqueId());
            temporaryNpcService.remove(player.getUniqueId());
            player.teleport(npc.getLocation());
            return;
        }
        if(temporaryNpcService.getNpcLoggedOffFile().getEntity().contains(player.getUniqueId())) {
            temporaryNpcService.removeLoggedOf(player);
            player.getInventory().clear();
        }

    }

}
