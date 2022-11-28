package in.prismar.game.item.impl;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GrenadeItem extends CustomItem {
    public GrenadeItem() {
        super("Grenade", Material.SLIME_BALL, "§6Grenade");
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.8f, 1);
        Vector vector = player.getLocation().getDirection().multiply(1.4);
        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(getMaterial()));
        item.setVelocity(vector);
        player.getInventory().setItemInMainHand(null);
        player.updateInventory();
        new BukkitRunnable() {
            int timer = 35;
            @Override
            public void run() {
                if(timer <= 0) {
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1f, 1);
                    for(Entity near : item.getWorld().getNearbyEntities(item.getLocation(), 7, 7, 7)) {
                        if(near instanceof Player target) {
                            double damage = 24 - target.getLocation().distance(item.getLocation());
                            target.damage(damage, player);
                        }
                    }
                    item.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, item.getLocation(), 2);
                    item.remove();
                    cancel();
                    return;
                }
                if(timer % 2 == 0) {
                    item.getWorld().playSound(item.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.4f, 2f);
                }
                item.getWorld().spawnParticle(Particle.SMOKE_NORMAL, item.getLocation(), 0);
                timer--;
            }
        }.runTaskTimer(game, 1, 1);
    }


}
