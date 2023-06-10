package in.prismar.game.item.impl.throwable;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.bus.ThrowableDeployEvent;
import in.prismar.game.item.event.bus.ThrowableExplodeEvent;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.spigot.item.ItemUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
@Setter
public abstract class ThrowableItem extends CustomItem {

    private Sound launchSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;
    private float launchSoundVolume = 0.8f;
    private double strength = 1.4;

    public ThrowableItem(String id, Material material, String display) {
        super(id, material, display);
        allFlags();
    }

    public boolean isAllowedToThrow(Player player, Game game) {
        return true;
    }

    public abstract void onThrow(ThrowEvent throwEvent);

    public boolean callExplodeEvent(ThrowEvent event, Location location) {
        ThrowableExplodeEvent explodeEvent = new ThrowableExplodeEvent(event.getPlayer(), this, location, false);
        event.getGame().getItemRegistry().getEventBus().publish(explodeEvent);
        if(explodeEvent.isCancelled()) {
            return true;
        }
        return false;
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        if(event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        ThrowableDeployEvent deployEvent = new ThrowableDeployEvent(player, this, false);
        game.getItemRegistry().getEventBus().publish(deployEvent);
        if(deployEvent.isCancelled()) {
            return;
        }
        if (!isAllowedToThrow(player, game)) {
            return;
        }
        player.getWorld().playSound(player.getLocation(), launchSound, launchSoundVolume, 1);
        Vector vector = player.getLocation().getDirection().multiply(strength);
        ItemStack stack = build();
        stack.setAmount(1);
        Item item = player.getWorld().dropItem(player.getEyeLocation(), stack);
        item.setPickupDelay(Integer.MAX_VALUE);
        item.setVelocity(vector);
        ItemUtil.takeItemFromHand(player, true);
        player.updateInventory();
        onThrow(new ThrowEvent(player, game, holder, event, item));
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
