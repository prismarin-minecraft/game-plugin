package in.prismar.game.ffa.model;

import in.prismar.library.spigot.hologram.Hologram;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
@NoArgsConstructor
public class GameMapPowerUp {

    private String id;
    private Location location;

    private transient long respawnTime;
    private transient Hologram hologram;

    public GameMapPowerUp(String id, Location location) {
        this.id = id;
        this.location = location;
    }
}
