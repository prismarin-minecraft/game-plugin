package in.prismar.game.item.impl.placeable;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.library.spigot.hologram.Hologram;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LandmineCustomItem extends PlaceableItem{

    private static final int DESPAWN_TIMER = 60;
    private static final double DAMAGE = 27;

    public LandmineCustomItem() {
        super("Landmine", Material.STICK, "§2Landmine");
        setCustomModelData(7);
        setPlacedItem(build());
    }

    @Override
    public boolean isAllowedToPlace(Player player, Game game) {
        if(!player.isOnGround()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou can only place the landmine on the ground.");
            return false;
        }
        return true;
    }

    @Override
    public void onPlace(Player player, Game game, Hologram hologram) {
        player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 0.7F, 0.2f);
        final long placed = System.currentTimeMillis() + 1000 * 2;
        new BukkitRunnable() {
            long ticks = 0;
            @Override
            public void run() {
                if(System.currentTimeMillis() < placed) {
                    return;
                }
                if(ticks >= 20 * DESPAWN_TIMER || !player.isOnline()) {
                    cancel();
                    hologram.disable();
                    return;
                }
                Location high = hologram.getLocation().clone().add(0, 1.7, 0);
                for(Player target : hologram.getLocation().getWorld().getPlayers()) {
                    if(high.distanceSquared(target.getLocation()) <= 2) {
                        ticks = 20 * DESPAWN_TIMER;
                        hologram.getLocation().getWorld().playSound(hologram.getLocation(), "grenade.explosion", 1.7f, 1f);
                        for(Entity near : hologram.getLocation().getWorld().getNearbyEntities(hologram.getLocation(), 7, 7, 7)) {
                            if(near instanceof Player nearTarget) {
                                double damage = DAMAGE - target.getLocation().distance(hologram.getLocation());
                                nearTarget.damage(damage, player);
                            }
                        }
                        hologram.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, high, 2);
                        return;
                    }
                }
                ticks+=5;
            }
        }.runTaskTimer(game, 5, 5);
    }
}
