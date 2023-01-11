package in.prismar.game.item.impl.gun.player;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.item.impl.gun.type.GunDamageType;
import lombok.Getter;
import lombok.Setter;
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
@Setter
public class GunPlayer {

    public static final Map<UUID, GunPlayer> PLAYERS = new HashMap<>();

    public static GunPlayer of(Player player) {
        return of(player.getUniqueId());
    }

    public static void remove(Player player) {
        PLAYERS.remove(player.getUniqueId());
    }

    public static GunPlayer of(UUID uuid) {
        if(PLAYERS.containsKey(uuid)) {
            return PLAYERS.get(uuid);
        }
        GunPlayer player = new GunPlayer();

        UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
        player.setUser(provider.getUserByUUID(uuid));
        PLAYERS.put(uuid, player);
        return player;
    }

    private User user;


    private boolean reloading;

    private boolean zooming;

    private String reloadingGunId = "";

    private long reloadingEndTimestamp;
    private long currentUpdateTick;

    private long lastInteract;

    private GunDamageType lastDamageReceived;



}
