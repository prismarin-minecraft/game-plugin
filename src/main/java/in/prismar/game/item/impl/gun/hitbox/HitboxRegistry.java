package in.prismar.game.item.impl.gun.hitbox;

import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitbox;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxFace;
import in.prismar.library.spigot.raytrace.result.RaytraceEntityHit;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HitboxRegistry {

    private final List<Hitbox> hitboxes = new ArrayList<>();

    public HitboxRegistry() {
        create(EntityType.IRON_GOLEM, "kindletron_2", 3f, 5f);
        create(EntityType.HUSK, "toro_type02", 4f, 7f);
        create(EntityType.HUSK, "zaku", 2.3f, 5f);
        create(EntityType.ZOMBIE, "giant_zombie", 2.3f, 4f);
        create(EntityType.IRON_GOLEM, "zahar", 2f, 5f);
        create(EntityType.IRON_GOLEM, "zahar_abomination", 2f, 5f);
    }

    public Hitbox create(EntityType type, double width, double height) {
        Hitbox hitbox = new Hitbox(type, width, height);
        this.hitboxes.add(hitbox);
        return hitbox;
    }

    public Hitbox create(EntityType type, String myhticMobId, double width, double height) {
        Hitbox hitbox = create(type, width, height);
        hitbox.setMyhticMobId(myhticMobId);
        return hitbox;
    }


    @Nullable
    public EntityHitbox getEntityHitbox(Entity entity) {
        for(Hitbox hitbox : this.hitboxes) {
            if(hitbox.getType() == entity.getType()) {
                if(hitbox.getMyhticMobId() != null) {
                    ActiveMob activeMob = MythicBukkit.inst().getMobManager().getActiveMob(entity.getUniqueId()).orElse(null);
                    if (activeMob == null) {
                        continue;
                    }
                    if (!activeMob.getType().getInternalName().equals(hitbox.getMyhticMobId())) {
                        continue;
                    }
                    return hitbox.create(entity);
                } else {
                    return hitbox.create(entity);
                }
            }
        }
        return null;
    }


    @Getter
    public class Hitbox {

        private final EntityType type;
        private final double width;
        private final double height;

        @Setter
        private String myhticMobId;

        public Hitbox(EntityType type, double width, double height) {
            this.type = type;
            this.width = width;
            this.height = height;
        }



        public EntityHitbox create(Entity entity) {
            return new EntityHitbox(entity, this.width, this.height);
        }
    }

    public class EntityHitbox implements RaytraceHitbox {

        private final Entity entity;

        private final Vector minVector;
        private final Vector maxVector;

        public EntityHitbox(Entity entity, double width, double height) {
            this.entity = entity;
            this.minVector = entity.getLocation().toVector().subtract(new Vector(width, 0f, width));
            this.maxVector = entity.getLocation().toVector().add(new Vector(width, height, width));
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
}
