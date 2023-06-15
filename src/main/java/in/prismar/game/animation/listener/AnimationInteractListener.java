package in.prismar.game.animation.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.animation.AnimationFacade;
import in.prismar.library.spigot.meta.anno.AutoListener;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@AutoListener
public class AnimationInteractListener implements Listener {


    @EventHandler
    public void onCall(PlayerInteractEvent event) {
        if(event.getItem() != null) {
            if(event.getItem().isSimilar(AnimationFacade.WAND_ITEM)) {
                Player player = event.getPlayer();
                if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "animation")) {
                    return;
                }
                UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
                User user = provider.getUserByUUID(player.getUniqueId());
                if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    user.setTag(AnimationFacade.LOCATION_A_KEY, event.getClickedBlock().getLocation());
                    player.sendMessage(PrismarinConstants.PREFIX + "§7Marked animation §aA §7position");
                    event.setCancelled(true);
                } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    user.setTag(AnimationFacade.LOCATION_B_KEY, event.getClickedBlock().getLocation());
                    player.sendMessage(PrismarinConstants.PREFIX + "§7Marked animation §6B §7position");
                    event.setCancelled(true);
                }
                return;
            }
        }
    }
}
