package in.prismar.game.map.model;

import in.prismar.api.map.GameMapLeaderboardEntry;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class GameMap extends StringRepositoryEntity {

    private ItemContainer icon;
    private List<Location> spawns;

    private List<GameMapPowerUp> powerUps;

    private transient List<GameMapLeaderboardEntry> leaderboard;

    private transient Map<UUID, GameMapPlayer> players;

    @Override
    public String toString() {
        return getId();
    }

    public Location getRandomSpawn() {
        return spawns.get(MathUtil.random(spawns.size() - 1));
    }

    public String getFancyName() {
        return icon.getItem().getItemMeta().getDisplayName();
    }
}
