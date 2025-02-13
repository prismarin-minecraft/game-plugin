package in.prismar.game.item.impl.gun;

import in.prismar.game.item.impl.gun.hitbox.HitboxRegistry;
import in.prismar.game.ai.AI;
import in.prismar.game.ai.hitbox.AIHitbox;
import in.prismar.game.ai.AIRegistry;
import in.prismar.game.ai.DamageableAI;
import in.prismar.game.warzone.combatlog.npc.TemporaryNpc;
import in.prismar.game.warzone.combatlog.npc.TemporaryNpcHitbox;
import in.prismar.game.warzone.combatlog.npc.TemporaryNpcService;
import in.prismar.library.spigot.raytrace.Raytrace;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceEntityHitbox;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitbox;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxHelper;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import in.prismar.library.spigot.raytrace.result.RaytraceResult;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class Bullet {

    private static final HitboxRegistry HITBOX_REGISTRY = new HitboxRegistry();

    private final Location origin;
    private final Location particleOrigin;
    private final Raytrace raytrace;
    private final Location endPoint;
    private final double range;
    private final Vector direction;

    public Bullet(Location particleOrigin, Location origin, Vector direction, double range) {
        this.particleOrigin = particleOrigin;
        this.origin = origin;
        this.range = range;
        this.direction = direction;
        this.endPoint = origin.clone().add(direction.multiply(range));

        this.raytrace = new Raytrace(origin, direction, new ArrayList<>());
        raytrace.getHitboxes().addAll(RaytraceHitboxHelper.collectPossibleEntityHitboxes(raytrace.getOrigin(), raytrace.getDirection(), range, 180));
        raytrace.getHitboxes().addAll(RaytraceHitboxHelper.collectPossibleBlockHitboxesProfessionalWay(raytrace.getOrigin(), raytrace.getDirection(), range));
        raytrace.getHitboxes().addAll(collectPossibleMobHitboxes(raytrace.getOrigin(), raytrace.getDirection(), range, 180));
        raytrace.getHitboxes().addAll(collectPossibleNpcHitboxes(raytrace.getOrigin(), raytrace.getDirection(), range, 180));
    }

    public static List<RaytraceHitbox> collectPossibleMobHitboxes(Location origin, Vector direction, double range, double fov) {
        final World world = origin.getWorld();
        final double rangeSqrt = range * range;
        final var diff = fov - 90;
        final var factor = (double) 4 / 10;
        final var result = diff * factor + 61;
        final var minDotProduct = Math.cos(Math.toRadians(result));
        List<RaytraceHitbox> hitboxes = new ArrayList<>();

        for (Entity entity : world.getNearbyEntities(origin, range, range, range, entity -> entity instanceof LivingEntity && !(entity instanceof Player))) {
            LivingEntity livingEntity = (LivingEntity) entity;
            Location location = livingEntity.getEyeLocation();
            if (location.distanceSquared(origin) <= rangeSqrt) {
                double deltaX = location.getX() - origin.getX();
                double deltaY = location.getY() - origin.getY();
                double deltaZ = location.getZ() - origin.getZ();
                Vector targetDirection = new Vector(deltaX, deltaY, deltaZ).normalize();
                double dot = targetDirection.dot(direction);
                if (dot > minDotProduct) {
                    HitboxRegistry.EntityHitbox hitbox = HITBOX_REGISTRY.getEntityHitbox(livingEntity);
                    if (hitbox != null) {
                        hitboxes.add(hitbox);
                        continue;
                    }
                    if (livingEntity.getType() == EntityType.ZOMBIE || livingEntity.getType() == EntityType.ZOMBIE_VILLAGER || livingEntity.getType() == EntityType.SKELETON) {
                        hitboxes.add(new RaytraceEntityHitbox(entity));
                    }
                }
            }

        }
        return hitboxes;
    }

    public static List<RaytraceHitbox> collectPossibleNpcHitboxes(Location origin, Vector direction, double range, double fov) {
        final double rangeSqrt = range * range;
        final var diff = fov - 90;
        final var factor = (double) 4 / 10;
        final var result = diff * factor + 61;
        final var minDotProduct = Math.cos(Math.toRadians(result));
        List<RaytraceHitbox> hitboxes = new ArrayList<>();

        for (TemporaryNpc npc : TemporaryNpcService.getInstance().getNpcs().values()) {
            Location location = npc.getEyeLocation();
            if (!origin.getWorld().getName().equals(location.getWorld().getName())) {
                continue;
            }
            if (location.distanceSquared(origin) <= rangeSqrt) {
                double deltaX = location.getX() - origin.getX();
                double deltaY = location.getY() - origin.getY();
                double deltaZ = location.getZ() - origin.getZ();
                Vector targetDirection = new Vector(deltaX, deltaY, deltaZ).normalize();
                double dot = targetDirection.dot(direction);
                if (dot > minDotProduct) {
                    hitboxes.add(new TemporaryNpcHitbox(npc));
                }
            }
        }

        for (AI ai : AIRegistry.getInstance().getAis()) {
            if (ai instanceof DamageableAI damageableAI) {
                LivingEntity livingEntity = (LivingEntity) ai.getNpc().getEntity();
                Location location = livingEntity.getEyeLocation();
                if (!origin.getWorld().getName().equals(location.getWorld().getName())) {
                    continue;
                }
                if (location.distanceSquared(origin) <= rangeSqrt) {
                    double deltaX = location.getX() - origin.getX();
                    double deltaY = location.getY() - origin.getY();
                    double deltaZ = location.getZ() - origin.getZ();
                    Vector targetDirection = new Vector(deltaX, deltaY, deltaZ).normalize();
                    double dot = targetDirection.dot(direction);
                    if (dot > minDotProduct) {
                        hitboxes.add(new AIHitbox(damageableAI));
                    }
                }
            }
        }
        return hitboxes;
    }


    public List<RaytraceHit> invoke() {
        RaytraceResult result = this.raytrace.ray(range);
        return result.getHits();
    }
}
