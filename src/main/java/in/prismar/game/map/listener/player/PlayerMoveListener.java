package in.prismar.game.map.listener.player;

import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerMoveListener implements Listener {

    final double CLIMBPITCH = -1.0, CLIMBSPEED = 0.6D;

    Set<UUID> wasOnLadder = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getPitch() < CLIMBPITCH && event.getTo().getY() - event.getFrom().getY() > 0.1D &&
                (player.getLocation().add(0.0D, 1.1D, 0.0D).getBlock().getType() == Material.END_ROD)) {
            player.setVelocity(player.getVelocity().clone().setY(CLIMBSPEED));
            wasOnLadder.add(player.getUniqueId());
            if(player.isSneaking()) {
                player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(0.5));
            }
            return;
        }
        if (player.getLocation().getPitch() > CLIMBPITCH && event.getTo().getY() - event.getFrom().getY() < 0.1D &&
                (player.getLocation().add(0.0D, -1.1D, 0.0D).getBlock().getType() == Material.END_ROD)) {
            player.setVelocity(player.getVelocity().clone().setY(-CLIMBSPEED));
            wasOnLadder.add(player.getUniqueId());
            if(player.isSneaking()) {
                player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(0.5));
            }
            return;
        }
        if(wasOnLadder.contains(player.getUniqueId())) {
            wasOnLadder.remove(player.getUniqueId());
        }
    }
}
