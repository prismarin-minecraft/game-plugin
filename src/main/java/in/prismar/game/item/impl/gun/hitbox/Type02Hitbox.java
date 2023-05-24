package in.prismar.game.item.impl.gun.hitbox;

import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitbox;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxFace;
import in.prismar.library.spigot.raytrace.result.RaytraceEntityHit;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Type02Hitbox implements RaytraceHitbox {
    private Entity entity;

    private Vector minVector;
    private Vector maxVector;

    public Type02Hitbox(Entity entity) {
        this.entity = entity;
        this.minVector = entity.getLocation().toVector().subtract(new Vector(5f, 0f, 5f));
        this.maxVector = entity.getLocation().toVector().add(new Vector(5f, 7f, 5f));
    }

    @Override
    public Vector getMaxVector() {
        return maxVector.clone();
    }

    @Override
    public Vector getMinVector() {
        return minVector.clone();
    }

    @Override
    public RaytraceHitboxFace[] getFaces() {
        return facesFromDefaultBox(minVector, maxVector);
    }

    @Override
    public RaytraceHit asHit(Location point) {
        return new RaytraceEntityHit(entity, point);
    }
}
