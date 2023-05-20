package in.prismar.game.item.impl.misc;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.raytrace.Raytrace;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxHelper;
import in.prismar.library.spigot.raytrace.result.RaytraceBlockHit;
import in.prismar.library.spigot.raytrace.result.RaytraceResult;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GrapplingGunItem extends CustomItem {


    public GrapplingGunItem() {
        super("GrapplingGun", Material.FISHING_ROD, "§6Grappling Gun");
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);

        UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(!user.isLocalTimestampAvailable("grappling.gun", System.currentTimeMillis() + 200)) {
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT, 0.5f, 1);

        final double range = 50;
        Raytrace raytrace = new Raytrace(player.getEyeLocation(), player.getEyeLocation().getDirection(),
                RaytraceHitboxHelper.collectPossibleBlockHitboxes(player.getEyeLocation(), player.getEyeLocation().getDirection(), range));
        RaytraceResult result = raytrace.ray(range);
        if(!result.getHits().isEmpty()) {
            if(result.getHits().stream().findFirst().get() instanceof RaytraceBlockHit blockHit) {
                Sheep target = player.getWorld().spawn(blockHit.getPoint(), Sheep.class);
                target.setAI(false);
                target.setBaby();
                target.setSilent(true);
                target.setInvisible(true);
                target.setInvulnerable(true);
                target.setGravity(false);
                target.setLeashHolder(player);
            }
        }

    }

}
