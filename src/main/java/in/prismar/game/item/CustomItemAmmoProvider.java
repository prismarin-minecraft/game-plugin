package in.prismar.game.item;

import in.prismar.game.Game;
import in.prismar.game.database.RedisContext;
import in.prismar.library.common.delayed.DelayedOperation;
import in.prismar.library.common.delayed.DelayedOperationExecutor;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.redisson.api.RMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class CustomItemAmmoProvider {

    @Inject
    private RedisContext redisContext;

    @Inject
    private Game game;

    private final Map<String, Integer> cache;
    private final Map<String, Integer> tempCache;
    private RMap<String, Integer> redisMap;
    private final DelayedOperationExecutor<DelayedOperation> executor;

    public CustomItemAmmoProvider() {
        this.cache = new HashMap<>();
        this.tempCache = new HashMap<>();
        this.executor = new DelayedOperationExecutor<>("CustomItemAmmo");
    }

    @SafeInitialize
    private void initialize() {
        this.redisMap = redisContext.getClient().getMap("custom_item_ammo");
    }

    public String getId(ItemStack stack) {
        String id = PersistentItemDataUtil.getString(game, stack, "ammo_id");
        if (id.isEmpty()) {
            id = UUID.randomUUID().toString() + System.currentTimeMillis();
            PersistentItemDataUtil.setString(game, stack, "ammo_id", id);
        }
        return id;
    }

    public boolean hasId(ItemStack stack) {
        String id = PersistentItemDataUtil.getString(game, stack, "ammo_id");
        return !id.isEmpty();
    }

    public int getAmmo(Player player, ItemStack stack) {
        String id = getId(stack);
        if(isTempCache(player)) {
            if(!tempCache.containsKey(id)) {
                tempCache.put(id, 0);
            }
            return tempCache.get(id);
        } else {
            if (!cache.containsKey(id)) {
                int ammo = 0;
                if (redisMap.containsKey(id)) {
                    ammo = redisMap.get(id);
                }
                cache.put(id, ammo);
            }
            return cache.get(id);
        }
    }

    public void setAmmo(Player player, ItemStack stack, int ammo) {
        String id = getId(stack);
        if(isTempCache(player)) {
            tempCache.put(id, ammo);
            return;
        }
        cache.put(id, ammo);
        executor.addTask(id, 30000, () -> {
           redisMap.put(id, getAmmo(player, stack));
        });
    }

    private boolean isTempCache(Player player) {
        return game.isCurrentlyPlayingAnyMode(player);
    }

}
