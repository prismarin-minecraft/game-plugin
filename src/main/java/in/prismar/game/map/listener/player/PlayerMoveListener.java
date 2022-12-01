package in.prismar.game.map.listener.player;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.map.GameMapFacade;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

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

    private UserProvider<User> provider;

    private final double climbPitch = -1.0, climbSpeed = 0.6D;

    private Set<UUID> wasOnLadder = new HashSet<>();

    public PlayerMoveListener() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!facade.isCurrentlyPlaying(player)) {
            return;
        }
        //Horizontal Zipline
        Location upper = player.getLocation().clone().add(0, 2, 0);
        if (upper.getBlock().getType() == Material.END_ROD) {
            player.setVelocity(player.getLocation().getDirection().multiply(climbSpeed).setY(0.5));
            return;
        }

        handleBalloon(player);


        if (player.getLocation().getPitch() < climbPitch && event.getTo().getY() - event.getFrom().getY() > 0.1D &&
                (player.getLocation().add(0.0D, 1.1D, 0.0D).getBlock().getType() == Material.END_ROD)) {
            player.setVelocity(player.getVelocity().clone().setY(climbSpeed));
            wasOnLadder.add(player.getUniqueId());
            if (player.isSneaking()) {
                player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(0.5));
            }
            return;
        }
        if (player.getLocation().getPitch() > climbPitch && event.getTo().getY() - event.getFrom().getY() < 0.1D &&
                (player.getLocation().add(0.0D, -1.1D, 0.0D).getBlock().getType() == Material.END_ROD)) {
            player.setVelocity(player.getVelocity().clone().setY(-climbPitch));
            wasOnLadder.add(player.getUniqueId());
            if (player.isSneaking()) {
                player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(0.5));
            }
            return;
        }
        if (wasOnLadder.contains(player.getUniqueId())) {
            wasOnLadder.remove(player.getUniqueId());
        }
    }

    private void handleBalloon(Player player) {
        if (!player.isOnGround()) {
            Location upper = player.getLocation().clone().add(0, 2, 0);
            if (player.getInventory().getChestplate() != null) {
                if (player.getInventory().getChestplate().getType() == Material.ELYTRA) {
                    player.getWorld().spawnParticle(Particle.SMOKE_NORMAL, player.getLocation(), 0);
                    return;
                }
            }
            if (upper.getBlock().getType() == Material.RED_CONCRETE) {
                User user = provider.getUserByUUID(player.getUniqueId());
                user.removeTag("doubleJump");
                player.setAllowFlight(false);
                player.setFlying(false);
                player.setVelocity(player.getLocation().getDirection().multiply(3).setY(0));
                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.5f, 1);
                player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_LAUNCH, 0.7f, 1);
                player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            }
        } else {
            if (player.getInventory().getChestplate() != null) {
                if (player.getInventory().getChestplate().getType() == Material.ELYTRA) {
                    User user = provider.getUserByUUID(player.getUniqueId());
                    boolean success = facade.giveArsenalChestplate(user, player);
                    if(!success) {
                        player.getInventory().setChestplate(new ItemStack(Material.AIR));
                    }
                    player.updateInventory();
                }
            }
        }

    }
}
