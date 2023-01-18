package in.prismar.game.tracer;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface BulletTracer {

    void play(Location start, Location end);
    ItemStack getIcon();
}
