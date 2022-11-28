package in.prismar.game.map.listener.player;

import in.prismar.game.map.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
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

    @Inject
    private GameMapFacade facade;

    private final double climbPitch = -1.0, climbSpeed = 0.6D;

    private Set<UUID> wasOnLadder = new HashSet<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(!facade.isCurrentlyPlaying(player)) {
            return;
        }
        if (player.getLocation().getPitch() < climbPitch && event.getTo().getY() - event.getFrom().getY() > 0.1D &&
                (player.getLocation().add(0.0D, 1.1D, 0.0D).getBlock().getType() == Material.END_ROD)) {
            player.setVelocity(player.getVelocity().clone().setY(climbSpeed));
            wasOnLadder.add(player.getUniqueId());
            if(player.isSneaking()) {
                player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(0.5));
            }
            return;
        }
        if (player.getLocation().getPitch() > climbPitch && event.getTo().getY() - event.getFrom().getY() < 0.1D &&
                (player.getLocation().add(0.0D, -1.1D, 0.0D).getBlock().getType() == Material.END_ROD)) {
            player.setVelocity(player.getVelocity().clone().setY(-climbPitch));
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
