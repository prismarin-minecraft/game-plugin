package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class MolotovItem extends CustomItem {
    public MolotovItem() {
        super("Molotov", Material.SPLASH_POTION, "§cMolotov");
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LINGERING_POTION_THROW, 0.8f, 1);
        Vector vector = player.getLocation().getDirection().multiply(1.4);
        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(getMaterial()));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(vector);
        ItemUtil.takeItemFromHand(player, true);
        player.updateInventory();
        new BukkitRunnable() {
            int saveTimer = 160;
            boolean spawned = false;
            List<Location> locations = new ArrayList<>();
            @Override
            public void run() {
                if(saveTimer <= 0) {
                    if(!item.isDead()) {
                       item.remove();
                    }
                    for(Location location : locations) {
                        if(location.getBlock().getType() == Material.FIRE) {
                            location.getBlock().setType(Material.AIR);
                        }
                    }
                    cancel();
                    return;
                }
                if(item.isOnGround() && !spawned) {
                    item.remove();
                    spawned = true;
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1f, 1);
                    for (int x = -3; x <= 3; x++) {
                        for (int z = -3; z <= 3; z++) {
                            Location location = item.getLocation().clone().add(x, 0, z);
                            if(location.getBlock().getType() == Material.AIR) {
                                location.getBlock().setType(Material.FIRE);
                                locations.add(location);
                            }
                        }
                    }
                    return;
                }
                if(saveTimer % 2 == 0 && !spawned) {
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.4f, 2f);
                }
                item.getWorld().spawnParticle(Particle.SMOKE_NORMAL, item.getLocation(), 0);
                saveTimer--;
            }
        }.runTaskTimer(game, 1, 1);
    }



}
