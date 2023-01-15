package in.prismar.game.hardpoint.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@NoArgsConstructor
public class Hardpoint {

    private Location location;
    private transient List<Location> circle;

    public Hardpoint(Location location) {
        this.location = location;
    }
}
