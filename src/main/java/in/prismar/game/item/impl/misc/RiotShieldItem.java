package in.prismar.game.item.impl.misc;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.raytrace.Raytrace;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxHelper;
import in.prismar.library.spigot.raytrace.result.RaytraceBlockHit;
import in.prismar.library.spigot.raytrace.result.RaytraceResult;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RiotShieldItem extends CustomItem {


    private final int shieldUpItem;
    private final int shieldItem;


    public RiotShieldItem() {
        super("RiotShield", Material.SHEARS, "§cRiot Shield");
        setCustomModelData(1);
        allFlags();
        this.shieldItem = 1;
        this.shieldUpItem = 2;
    }

    @CustomItemEvent
    public void onSneak(Player player, Game game, CustomItemHolder holder, PlayerToggleSneakEvent event) {
        if(holder.getHoldingType() != CustomItemHoldingType.LEFT_HAND && holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        GunPlayer gunPlayer = GunPlayer.of(player);
        gunPlayer.setShielded(event.isSneaking());
        updateItem(player, holder, gunPlayer.isShielded());
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 0.6f, 1);
    }

    private void updateItem(Player player, CustomItemHolder holder, boolean state) {
        ItemStack stack = holder.getStack();
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(state ? shieldUpItem : shieldItem);
        stack.setItemMeta(meta);
        player.updateInventory();
    }

}
