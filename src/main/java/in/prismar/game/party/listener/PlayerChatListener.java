package in.prismar.game.party.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.party.Party;
import in.prismar.game.party.PartyRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerChatListener implements Listener {

    @Inject
    private PartyRegistry registry;


    @EventHandler(priority = EventPriority.NORMAL)
    public void onCall(AsyncPlayerChatEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        if (registry.isPartyChatToggled(player)) {
            Party party = registry.getPartyByPlayer(player);
            if (party != null) {
                event.setCancelled(true);
                registry.sendMessage(party, PartyRegistry.PARTY_CHAT_PREFIX + player.getName() + " " + PrismarinConstants.ARROW_RIGHT +" §d" + event.getMessage());
            }

        }
    }
}
