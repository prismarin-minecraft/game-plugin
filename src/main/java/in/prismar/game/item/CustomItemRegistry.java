package in.prismar.game.item;

import in.prismar.game.Game;
import in.prismar.game.item.gun.Gun;
import in.prismar.game.item.gun.impl.G36Gun;
import in.prismar.game.item.gun.impl.L96Gun;
import in.prismar.game.item.gun.impl.Spas12Gun;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.util.PersistentItemDataUtil;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service
public class CustomItemRegistry {

    private final Game game;
    private Map<String, CustomItem> items;
    private Map<UUID, List<CustomItemHolder>> holders;

    public CustomItemRegistry(Game game) {
        this.game = game;
        this.items = new HashMap<>();
        this.holders = new HashMap<>();
        load();

        Bukkit.getScheduler().runTaskTimer(game, new CustomItemUpdater(game, this), 1, 1);
    }

    private void load() {
        register(new G36Gun());
        register(new L96Gun());
        register(new Spas12Gun());
    }


    public void publishEvent(Player player, Object event) {
        if(holders.containsKey(player.getUniqueId())) {
            for(CustomItemHolder holder : holders.get(player.getUniqueId())) {
                holder.getItem().getEventBus().publish(player, game, holder, event);
            }
        }
    }
    public List<CustomItemHolder> scan(Player player) {
        List<CustomItemHolder> items = new ArrayList<>();
        for(ItemStack stack : player.getInventory().getContents()) {
            CustomItem item = getItemByStack(stack);
            if(item != null) {
                if(isItemInHand(player, stack, true)) {
                    items.add(new CustomItemHolder(item, stack, CustomItemHoldingType.RIGHT_HAND));
                } else if(isItemInHand(player, stack, false)) {
                    items.add(new CustomItemHolder(item, stack, CustomItemHoldingType.LEFT_HAND));
                } else {
                    items.add(new CustomItemHolder(item, stack, CustomItemHoldingType.INVENTORY));
                }
            }
        }
        for(ItemStack stack : player.getInventory().getArmorContents()) {
            CustomItem item = getItemByStack(stack);
            if(item != null) {
                items.add(new CustomItemHolder(item, stack, CustomItemHoldingType.ARMOR));
            }
        }
        holders.put(player.getUniqueId(), items);
        return items;
    }

    private boolean isItemInHand(Player player, ItemStack stack, boolean right) {
        ItemStack itemStack = right ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        if(itemStack != null) {
            if(itemStack.hasItemMeta()) {
                if(itemStack.getItemMeta().hasDisplayName()) {
                    return itemStack.getItemMeta().getDisplayName().equals(stack.getItemMeta().getDisplayName());
                }
            }
        }
        return false;
    }

    public void register(CustomItem item) {
        this.items.put(item.getId().toLowerCase(), item);
    }

    public CustomItem getItemById(String id) {
        return this.items.get(id.toLowerCase());
    }

    public boolean existsItemById(String id) {
        return this.items.containsKey(id.toLowerCase());
    }

    public boolean existsItemByIdIgnoreCase(String id) {
        for (CustomItem item : items.values()) {
            if (item.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public CustomItem getItemByDisplayName(String displayName) {
        for(CustomItem customItem : getItems().values()) {
            if(customItem.getDisplayName().equals(displayName)) {
                return customItem;
            }
        }
        return null;
    }

    public CustomItem getItemByStack(ItemStack stack) {
        if(stack != null) {
            if(stack.hasItemMeta()) {
                if(stack.getItemMeta().hasDisplayName()) {
                    return getItemByDisplayName(stack.getItemMeta().getDisplayName());
                }
            }
        }
        return null;
    }

    public ItemStack createItem(String id) {
        return getItemById(id.toLowerCase()).build();
    }

    public ItemStack createGunItem(String id) {
        CustomItem customItem = getItemById(id.toLowerCase());
        if(customItem instanceof Gun gun) {
            ItemStack item = getItemById(id.toLowerCase()).build();
            PersistentItemDataUtil.setInteger(game, item, "ammo", gun.getMaxAmmo());
            return item;
        }
        return customItem.build();

    }
}
