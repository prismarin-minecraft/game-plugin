package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Setter
public class ThrowableItem extends CustomItem {

    private Sound launchSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;
    private float launchSoundVolume = 0.8f;
    private double strength = 1.4;

    private Consumer<ThrowEvent> onThrow;

    public ThrowableItem(String id, Material material, String display) {
        super(id, material, display);
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);

        player.getWorld().playSound(player.getLocation(), launchSound, launchSoundVolume, 1);
        Vector vector = player.getLocation().getDirection().multiply(strength);
        Item item = player.getWorld().dropItem(player.getEyeLocation(), event.getItem().clone());
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(vector);
        ItemUtil.takeItemFromHand(player, true);
        player.updateInventory();
        if(onThrow != null) {
            onThrow.accept(new ThrowEvent(player, game, holder, event, item));
        }
    }


    @Getter
    @AllArgsConstructor
    public static class ThrowEvent {

        private Player player;
        private Game game;
        private CustomItemHolder holder;
        private PlayerInteractEvent event;
        private Item item;

    }

}
