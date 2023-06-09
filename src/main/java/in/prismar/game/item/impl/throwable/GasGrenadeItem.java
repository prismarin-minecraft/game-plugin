package in.prismar.game.item.impl.throwable;

import in.prismar.game.Game;
import in.prismar.game.item.event.bus.ThrowableExplodeEvent;
import in.prismar.library.common.math.MathUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GasGrenadeItem extends LethalItem {

    private static final Particle.DustOptions PARTICLE_OPTIONS = new Particle.DustOptions(Color.GREEN, 6);
    private static final double RANGE = 3.7;
    private static final double DAMAGE = 7;

    public GasGrenadeItem() {
        super("GasGrenade", Material.STICK, "§2Gas Grenade");
        setCustomModelData(5);
        allFlags();
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Item item = throwEvent.getItem();
        Game game = throwEvent.getGame();
        final ThrowableItem throwableItem = this;
        new BukkitRunnable() {
            int saveTimer = 180;
            boolean spawned = false;

            @Override
            public void run() {
                if (saveTimer <= 0 || !throwEvent.getPlayer().isOnline()) {
                    if (!item.isDead()) {
                        item.remove();
                    }
                    cancel();
                    return;
                }
                if (item.isOnGround()) {
                    if (!spawned) {
                        item.remove();
                        ThrowableExplodeEvent explodeEvent = new ThrowableExplodeEvent(throwEvent.getPlayer(), throwableItem, item.getLocation(), false);
                        game.getItemRegistry().getEventBus().publish(explodeEvent);
                        if(explodeEvent.isCancelled()) {
                            cancel();
                            return;
                        }
                        spawned = true;
                        item.getWorld().playSound(item.getLocation(), "grenade.gas", 2f, 1f);
                        return;
                    }
                    if (saveTimer % 10 == 0) {
                        for (Entity entity : item.getNearbyEntities(RANGE, RANGE, RANGE)) {
                            if (entity instanceof LivingEntity target) {
                                target.damage(DAMAGE, throwEvent.getPlayer());
                                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 1));
                                target.setVelocity(new Vector());
                            }
                        }
                    }

                    Location l = item.getLocation().clone();

                    for (int i = 0; i < 5; i++) {
                        double randX = MathUtil.randomDouble(-RANGE, RANGE);
                        double randY = MathUtil.randomDouble(-RANGE, RANGE);
                        double randZ = MathUtil.randomDouble(-RANGE, RANGE);
                        l.getWorld().spawnParticle(Particle.REDSTONE, l.getX() + randX, l.getY() + randY, l.getZ() + randZ, 1, PARTICLE_OPTIONS);
                    }

                }
                if (saveTimer % 2 == 0 && !spawned) {
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 0.8f, 2f);
                }
                saveTimer--;
            }
        }.runTaskTimer(game, 1, 1);
    }
}
