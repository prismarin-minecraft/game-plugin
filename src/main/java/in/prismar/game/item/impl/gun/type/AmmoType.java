package in.prismar.game.item.impl.gun.type;

import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
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

    PISTOL(new ItemBuilder(Material.ARROW).setCustomModelData(1).setName("§7Pistol Ammo").build(), 10),
    AR(new ItemBuilder(Material.ARROW).setCustomModelData(2).setName("§eAR Ammo").build(), 7),
    LMG(new ItemBuilder(Material.ARROW).setCustomModelData(2).setName("§eAR Ammo").build(), 7),
    RAIL(new ItemBuilder(Material.ARROW).setCustomModelData(5).setName("§cRail").build(), 0),
    SNIPER(new ItemBuilder(Material.ARROW).setCustomModelData(3).setName("§cSniper Ammo").build(), 1),
    SHOTGUN(new ItemBuilder(Material.ARROW).setCustomModelData(4).setName("§fShotgun Ammo").build(), 2),

    SMG(new ItemBuilder(Material.ARROW).setCustomModelData(1).setName("§7Pistol Ammo").build(), 10),
    GRENADE_LAUNCHER(new ItemBuilder(Material.STICK).setCustomModelData(6).setName("§cImpact Grenade").allFlags().build(), 1);

    private final ItemStack item;
    private final int refillStacks;

    public static int getAmmoInInventory(Player player, AmmoType type) {
        int amount = 0;
        for(ItemStack stack : player.getInventory()) {
            if(stack != null) {
                if(ItemUtil.compare(type.getItem(), stack)) {
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
                if(ItemUtil.compare(type.getItem(), stack)) {
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
