package in.prismar.game.tracer.impl;

import in.prismar.game.tracer.AbstractBulletTracer;
import in.prismar.game.tracer.BulletTracer;
import in.prismar.library.spigot.particle.ParticleUtil;
import lombok.AllArgsConstructor;
import net.minecraft.core.particles.ParticleOptions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Consumer;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ColoredBulletTracer extends AbstractBulletTracer {

    private final Particle.DustOptions options;

    public ColoredBulletTracer(ItemStack icon, Color color, int size) {
        super(icon);
        this.options = new Particle.DustOptions(color, size);
    }

    @Override
    public void play(Location start, Location end) {
        ParticleUtil.spawnParticleAlongLine(start, end, 20, location -> {
            location.getWorld().spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1, options);
        });
    }
}
