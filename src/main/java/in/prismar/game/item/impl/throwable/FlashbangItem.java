package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FlashbangItem extends LethalItem {

    private static final int TIME = 80;

    public FlashbangItem() {
        super("FlashbangItem", Material.STICK, "§fFlashbang");
        setCustomModelData(8);
        allFlags();
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Item item = throwEvent.getItem();
        Game game = throwEvent.getGame();
        new BukkitRunnable() {

            long ticks = 0;
            @Override
            public void run() {
                if (!throwEvent.getPlayer().isOnline() || ticks >= 20 * 60) {
                    if (!item.isDead()) {
                        item.remove();
                    }
                    cancel();
                    return;
                }
                if (item.isOnGround()) {
                    item.remove();
                    cancel();
                    if(callExplodeEvent(throwEvent, item.getLocation())) {
                        return;
                    }
                    item.getWorld().playSound(item.getLocation(), "grenade.flashbang", 1.7f, 1f);
                    for(Entity near : item.getWorld().getNearbyEntities(item.getLocation(), 9, 9, 9)) {
                        if(near instanceof Player target) {
                            int time = TIME - ((int)target.getLocation().distance(item.getLocation()) * 7);
                            if(time > 0) {
                                target.sendTitle("§f語", "", 0, time, 50);
                            }
                        }
                    }
                    item.getWorld().spawnParticle(Particle.CLOUD, item.getLocation(), 2);
                    return;
                }
                ticks++;
            }
        }.runTaskTimer(game, 1, 1);
    }
}
