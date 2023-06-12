package in.prismar.game.ffa.model;

import in.prismar.api.map.GameMapLeaderboardEntry;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Data;
import org.bukkit.Location;

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
public class FFAMap extends StringRepositoryEntity {

    private ItemContainer icon;
    private List<Location> spawns;

    private List<FFAMapPowerUp> powerUps;

    private transient List<GameMapLeaderboardEntry> leaderboard;

    private transient Map<UUID, FFAMapPlayer> players;

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
