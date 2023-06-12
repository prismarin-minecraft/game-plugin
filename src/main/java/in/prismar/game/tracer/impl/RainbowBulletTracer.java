package in.prismar.game.tracer.impl;

import in.prismar.game.tracer.AbstractBulletTracer;
import in.prismar.game.tracer.BulletTracer;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.particle.ParticleUtil;
import lombok.AllArgsConstructor;
import net.minecraft.core.particles.ParticleOptions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.util.Consumer;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/

public class RainbowBulletTracer extends AbstractBulletTracer {

    private final List<Particle.DustOptions> options;

    public RainbowBulletTracer() {
        super(new ItemBuilder(Material.MAGENTA_WOOL).setName("§2R§6a§di§bn§9b§ao§5w").build());
        this.options = new ArrayList<>();
        this.options.add(new Particle.DustOptions(Color.RED, 1));
        this.options.add(new Particle.DustOptions(Color.BLUE, 1));
        this.options.add(new Particle.DustOptions(Color.YELLOW, 1));
        this.options.add(new Particle.DustOptions(Color.LIME, 1));
        this.options.add(new Particle.DustOptions(Color.NAVY, 1));
        this.options.add(new Particle.DustOptions(Color.PURPLE, 1));
        this.options.add(new Particle.DustOptions(Color.ORANGE, 1));
    }

    @Override
    public void play(Location start, Location end) {
        ParticleUtil.spawnParticleAlongLine(start, end, 20, location -> {
            location.getWorld().spawnParticle(Particle.REDSTONE, location.getX(), location.getY(), location.getZ(), 1,
                    options.get(MathUtil.random(options.size()-1)));
        });
    }
}
