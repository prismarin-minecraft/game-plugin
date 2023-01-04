package in.prismar.game.leaderboard.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.hologram.Hologram;
import lombok.Data;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class Leaderboard extends StringRepositoryEntity {

    private String title;
    private String format;
    private String sorted;
    private int size;
    private Location location;

    private transient Hologram hologram;

    @Override
    public String toString() {
        return getId();
    }
}
