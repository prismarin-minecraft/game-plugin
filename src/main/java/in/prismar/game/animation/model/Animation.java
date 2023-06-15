package in.prismar.game.animation.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.location.Cuboid;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter @Setter
public class Animation extends StringRepositoryEntity {

    private int ticks;
    private boolean stay;

    private Location locationA;
    private Location locationB;
    private List<AnimationFrame> frames;

    private transient Cuboid cuboid;

    @Override
    public String toString() {
        return getId();
    }
}
