package in.prismar.game.item.impl.throwable;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AirdropItem extends CustomItem {
    public AirdropItem() {
        super("Airdrop", Material.STICK, "§eAirdrop");
        setCustomModelData(1);
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        if(!game.getExtractionFacade().isIn(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou can only call an airdrop inside extraction.");
            return;
        }


        player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.8f, 2f);
        Vector vector = player.getLocation().getDirection().multiply(1.4);
        Item item = player.getWorld().dropItem(player.getEyeLocation(), new ItemStack(getMaterial()));
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(vector);
        ItemUtil.takeItemFromHand(player, true);
        player.updateInventory();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(item.isOnGround()) {
                    cancel();
                    item.remove();
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3f, 1);
                    game.getAirDropRegistry().callAirDrop(item.getLocation());
                    return;
                }
                item.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, item.getLocation(), 0);
            }
        }.runTaskTimer(game, 1, 1);
    }

    private ArmorStand spawnMissile(ItemStack stack, Location location) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(true);
        armorStand.getEquipment().setHelmet(stack);
        return armorStand;
    }


}
