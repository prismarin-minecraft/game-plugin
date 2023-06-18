package in.prismar.game.unlockable;

import lombok.Getter;
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
public class Unlockable {

    private String id;
    private String displayName;
    private Location location;

    @Override
    public String toString() {
        return id;
    }
}
