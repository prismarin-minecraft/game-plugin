package in.prismar.game.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public final class PersistentItemDataUtil {

    private static final Map<String, NamespacedKey> CACHED_KEYS = new HashMap<>();

    public static void setInteger(Plugin plugin, ItemStack stack, String key, int value) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!CACHED_KEYS.containsKey(key)) {
            CACHED_KEYS.put(key, new NamespacedKey(plugin, key));
        }
        container.set(CACHED_KEYS.get(key), PersistentDataType.INTEGER, value);
        stack.setItemMeta(meta);

    }

    public static int getInteger(Plugin plugin, ItemStack stack, String key) {
        ItemMeta meta = stack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if(!CACHED_KEYS.containsKey(key)) {
            CACHED_KEYS.put(key, new NamespacedKey(plugin, key));
        }
        NamespacedKey namespacedKey = CACHED_KEYS.get(key);
        if(container.has(namespacedKey, PersistentDataType.INTEGER)) {
            return container.get(namespacedKey, PersistentDataType.INTEGER);
        }
        return 0;
    }
}
