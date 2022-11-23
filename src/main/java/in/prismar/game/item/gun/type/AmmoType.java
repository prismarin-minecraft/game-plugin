package in.prismar.game.item.gun.type;

import in.prismar.library.spigot.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
public enum AmmoType {

    PISTOL(new ItemBuilder(Material.FLINT).setName("§7Pistol Ammo").build()),
    AR(new ItemBuilder(Material.GLOWSTONE_DUST).setName("§eAR Ammo").build()),
    SNIPER(new ItemBuilder(Material.REDSTONE).setName("§cSniper Ammo").build()),
    SHOTGUN(new ItemBuilder(Material.BLAZE_POWDER).setName("§fShotgun Ammo").build());

    private final ItemStack item;

    public static int getAmmoInInventory(Player player, AmmoType type) {
        int amount = 0;
        for(ItemStack stack : player.getInventory()) {
            if(stack != null) {
                if(stack.isSimilar(type.getItem())) {
                    amount += stack.getAmount();
                }
            }
        }
        return amount;
    }

    public static void takeAmmo(Player player, AmmoType type, int amount) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if(stack != null) {
                if(stack.isSimilar(type.getItem())) {
                   if(amount >= stack.getAmount()) {
                       amount -= stack.getAmount();
                       player.getInventory().setItem(i, null);
                   } else {
                       stack.setAmount(stack.getAmount() - amount);
                       return;
                   }
                }
            }
        }
        player.updateInventory();
    }

    public static AmmoType getAmmoType(String name) {
        for(AmmoType type : AmmoType.values()) {
            if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

}
