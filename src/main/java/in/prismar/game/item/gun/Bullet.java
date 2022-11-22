package in.prismar.game.weapon;

import in.prismar.game.raytrace.Raytrace;
import in.prismar.game.raytrace.hitbox.RaytraceEntityHitbox;
import in.prismar.game.raytrace.hitbox.RaytraceHitbox;
import in.prismar.game.raytrace.hitbox.RaytraceHitboxHelper;
import in.prismar.game.raytrace.result.RaytraceHit;
import in.prismar.game.raytrace.result.RaytraceResult;
import in.prismar.game.util.ParticleUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;
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

    private Raytrace raytrace;
    private Location endPoint;

    public Bullet(Particle particle, Location origin, Vector direction, double range, Consumer<List<RaytraceHit>> consumer) {
        this.endPoint = origin.clone().add(direction.multiply(range));
        ParticleUtil.spawnParticleAlongLine(origin, endPoint, particle, 20, 0);
        this.raytrace = new Raytrace(origin, direction);
        RaytraceResult result = this.raytrace.ray(range);
        result.getHits().sort(Comparator.comparingDouble(o -> o.getPoint().distanceSquared(origin)));
        consumer.accept(result.getHits());
    }
}
