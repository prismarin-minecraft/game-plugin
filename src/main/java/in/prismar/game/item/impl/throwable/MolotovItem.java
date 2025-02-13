package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class MolotovItem extends LethalItem {
    public MolotovItem() {
        super("Molotov", Material.STICK, "§cMolotov");
        setCustomModelData(3);
        setLaunchSound(Sound.ENTITY_LINGERING_POTION_THROW);
        allFlags();
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Item item = throwEvent.getItem();
        Game game = throwEvent.getGame();
        new BukkitRunnable() {
            int saveTimer = 160;
            boolean spawned = false;
            final List<Location> locations = new ArrayList<>();
            @Override
            public void run() {
                if(saveTimer <= 0) {
                    if(!item.isDead()) {
                        item.remove();
                    }
                    for(Location location : locations) {
                        if(location.getBlock().getType() == Material.FIRE) {
                            location.getBlock().setType(Material.AIR);
                        }
                    }
                    cancel();
                    return;
                }
                if(item.isOnGround() && !spawned) {
                    item.remove();
                    spawned = true;
                    if(callExplodeEvent(throwEvent, item.getLocation())) {
                        cancel();
                        return;
                    }

                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 1f, 1);
                    for (int x = -3; x <= 3; x++) {
                        for (int z = -3; z <= 3; z++) {
                            Location location = item.getLocation().clone().add(x, 0, z);
                            if(location.getBlock().getType() == Material.AIR) {
                                location.getBlock().setType(Material.FIRE);
                                locations.add(location);
                            }
                        }
                    }
                    return;
                }
                if(saveTimer % 2 == 0 && !spawned) {
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.4f, 2f);
                }
                item.getWorld().spawnParticle(Particle.SMOKE_NORMAL, item.getLocation(), 0);
                saveTimer--;
            }
        }.runTaskTimer(game, 1, 1);
    }
}
