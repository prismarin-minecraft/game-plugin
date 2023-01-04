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
public class AirdropItem extends ThrowableItem {
    public AirdropItem() {
        super("Airdrop", Material.STICK, "§eAirdrop");
        setCustomModelData(1);
        allFlags();


    }

    @Override
    public boolean isAllowedToThrow(Player player, Game game) {
        if(!game.getExtractionFacade().isIn(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou can only call an airdrop inside extraction.");
            return false;
        }
        return true;
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Item item = throwEvent.getItem();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(item.isOnGround()) {
                    cancel();
                    item.remove();
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3f, 1);
                    throwEvent.getGame().getAirDropRegistry().callAirDrop(item.getLocation());
                    return;
                }
                item.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, item.getLocation(), 0);
            }
        }.runTaskTimer(throwEvent.getGame(), 1, 1);
    }




}
