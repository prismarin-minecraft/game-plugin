package in.prismar.game.party.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.party.Party;
import in.prismar.game.party.PartyRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerQuitListener implements Listener {

    @Inject
    private PartyRegistry registry;

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Party party = registry.getPartyByPlayer(player);
        if(party != null) {
            boolean wasOwner = party.getOwner().getUniqueId().equals(player.getUniqueId());
            boolean disband = registry.leave(party, player);
            registry.sendMessage(party, PrismarinConstants.PREFIX + "§3" + player.getName() + " §7left the party");
            if(!disband) {
                if(wasOwner) {
                    registry.sendMessage(party, PrismarinConstants.PREFIX + "§b" + party.getOwner().getName() + " §7is the new owner of the party");
                }
            }
        }
    }
}
