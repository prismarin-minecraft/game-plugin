package in.prismar.game.item.impl.gun;

import in.prismar.library.spigot.particle.ParticleUtil;
import in.prismar.library.spigot.raytrace.Raytrace;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import in.prismar.library.spigot.raytrace.result.RaytraceResult;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.Comparator;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Bullet {

    private Location origin;
    private Location particleOrigin;
    private Raytrace raytrace;
    private Location endPoint;
    private double range;

    public Bullet(Location particleOrigin, Location origin, Vector direction, double range) {
        this.particleOrigin = particleOrigin;
        this.origin = origin;
        this.range = range;
        this.endPoint = origin.clone().add(direction.multiply(range));

        this.raytrace = new Raytrace(origin, direction);
    }

    public List<RaytraceHit> invoke() {
        RaytraceResult result = this.raytrace.ray(range);
        return result.getHits();
    }
}
