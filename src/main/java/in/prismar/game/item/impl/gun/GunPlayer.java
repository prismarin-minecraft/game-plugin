package in.prismar.game.item.impl.gun;

import in.prismar.game.item.impl.gun.type.GunDamageType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class GunPlayer {

    private static final Map<UUID, GunPlayer> PLAYERS = new HashMap<>();

    public static GunPlayer of(Player player) {
        return of(player.getUniqueId());
    }

    public static GunPlayer of(UUID uuid) {
        if(PLAYERS.containsKey(uuid)) {
            return PLAYERS.get(uuid);
        }
        GunPlayer player = new GunPlayer();
        PLAYERS.put(uuid, player);
        return player;
    }

    private boolean reloading;
    private long reloadingEndTimestamp;
    private long currentUpdateTick;

    private long lastInteract;

    private GunDamageType lastDamageReceived;


}
