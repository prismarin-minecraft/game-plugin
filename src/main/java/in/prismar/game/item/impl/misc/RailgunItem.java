package in.prismar.game.item.impl.misc;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.common.text.Progress;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.C;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class RailgunItem extends CustomItem {

    private static final int MAX_AMMO = 5;

    private static final double DAMAGE = 40;
    private static final double EXPLOSION_RANGE = 8;

    private static final long MAX_PROGRESS = 60;
    private static final long COUNTDOWN = 1000 * 4;

    private static final Progress PROGRESS = new Progress("§8[§7=§7=§7=§7=§7=§7=§7=§7=§7=§7=§7=§7=§7=§7=§7=§8]", 15, "§7=", "§a=", false);

    public RailgunItem() {
        super("Railgun", Material.DIAMOND_AXE, "§4§lRailgun");
        setCustomModelData(1);
        allFlags();
    }

    @CustomItemEvent
    public void onInteract(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        GunPlayer gunPlayer = GunPlayer.of(player.getUniqueId());
        gunPlayer.setLastInteract(System.currentTimeMillis());
    }

    @CustomItemEvent
    public void onUpdate(Player player, Game game, CustomItemHolder holder, CustomItemHolder event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        GunPlayer gunPlayer = GunPlayer.of(player.getUniqueId());
        if(gunPlayer.getUser().containsTag("railgunCooldown")) {
            long time = gunPlayer.getUser().getTag("railgunCooldown");
            if(System.currentTimeMillis() < time) {
                gunPlayer.setCurrentUpdateTick(0);
                return;
            }
        }
        int ammo = PersistentItemDataUtil.getInteger(game, holder.getStack(), Gun.AMMO_KEY);
        if(ammo == 0) {
            PersistentItemDataUtil.setInteger(game, holder.getStack(), Gun.AMMO_KEY, MAX_AMMO);
            ammo = MAX_AMMO;
        }

        long difference = System.currentTimeMillis() - gunPlayer.getLastInteract();
        if(difference <= 300) {
            if(gunPlayer.getCurrentUpdateTick() >= MAX_PROGRESS) {
                gunPlayer.getUser().setTag("railgunCooldown", System.currentTimeMillis() + COUNTDOWN);
                player.getWorld().playSound(player.getLocation(), "railgun.shoot", 1f, 1f);
                new ParticleShooter(game, player, player.getEyeLocation(), player.getEyeLocation().getDirection(), location -> {
                    location.getWorld().playSound(location, "grenade.explosion", 3f, 1f);
                    location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1);
                    for(Entity near : location.getWorld().getNearbyEntities(location, EXPLOSION_RANGE, EXPLOSION_RANGE, EXPLOSION_RANGE)) {
                        if(near instanceof LivingEntity target) {
                            double damage = DAMAGE - target.getLocation().distance(location);
                            target.damage(damage, player);
                        }
                    }
                });
                ammo--;
                if(ammo <= 0) {
                    player.getInventory().setItemInMainHand(null);
                    player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 0.3f, 1);
                    player.updateInventory();
                } else {
                    PersistentItemDataUtil.setInteger(game, holder.getStack(), Gun.AMMO_KEY, ammo);
                }
                return;
            }
            if(gunPlayer.getCurrentUpdateTick() == 0) {
                player.playSound(player.getLocation(), "railgun.charge", 1f, 1f);
            }
            gunPlayer.setCurrentUpdateTick(gunPlayer.getCurrentUpdateTick() + 1);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(PROGRESS.show(gunPlayer.getCurrentUpdateTick(), MAX_PROGRESS)));
            if(gunPlayer.getCurrentUpdateTick() % 5 == 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 2, false, false));
            }
            return;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Railgun shots§8: §c§l" + ammo));
        gunPlayer.setCurrentUpdateTick(0);
    }

    public class ParticleShooter extends BukkitRunnable {

        private static final int DURATION_IN_TICKS = 300;
        private static final double RADIUS = 1.5D;
        private static final double ROTATION_DEGREES_PER_TICK = 20;
        private static final double TRAVEL_DISTANCE_PER_TICK = 1D;

        private int ticks = 0;

        private final Vector dir;
        private final Player shooter;
        private final Location currentLoc;
        private final Consumer<Location> consumer;

        public ParticleShooter(Plugin plugin, Player player,  Location origin, Vector dir, Consumer<Location> consumer) {
            this.consumer = consumer;
            this.currentLoc = origin.clone();
            this.dir = dir.clone().normalize();
            this.shooter = player;

            this.runTaskTimer(plugin, 0L, 1L);
        }

        @Override
        public void run() {
            if (ticks++ >= DURATION_IN_TICKS || currentLoc.getWorld() == null) {
                this.cancel();
                return;

            }
            if(currentLoc.getBlock().getType().isSolid()) {
                consumer.accept(currentLoc);
                cancel();
                return;
            }

            for(Entity near : currentLoc.getWorld().getNearbyEntities(currentLoc, RADIUS, RADIUS, RADIUS)) {
                if(near instanceof Player target) {
                    if(!target.getUniqueId().equals(shooter.getUniqueId())) {
                        consumer.accept(currentLoc);
                        cancel();
                        return;
                    }

                }
            }

            currentLoc.getWorld().spawnParticle(Particle.REDSTONE, currentLoc, 1, new Particle.DustOptions(Color.RED, 4));


            Vector offset = getPerpendicular(dir).multiply(RADIUS).rotateAroundAxis(dir, Math.toRadians(ROTATION_DEGREES_PER_TICK * ticks));
            for (int i =0 ; i < 3; i++)
                currentLoc.getWorld().spawnParticle(Particle.REDSTONE, currentLoc.clone().add(offset), 1,
                        new Particle.DustOptions(Color.GRAY, 2));

            currentLoc.add(dir.clone().multiply(TRAVEL_DISTANCE_PER_TICK));
        }

        private Vector getPerpendicular(Vector vec) {
            if (!vec.isNormalized())
                vec = vec.clone().normalize();

            return Math.abs(vec.getZ()) < Math.abs(vec.getX()) ?
                    new Vector(vec.getY(), -vec.getX(), 0) : new Vector(0, -vec.getZ(), vec.getY());
        }
    }


}
