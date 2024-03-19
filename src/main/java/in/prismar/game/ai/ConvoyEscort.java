package in.prismar.game.ai;

import in.prismar.game.Game;
import in.prismar.game.item.impl.gun.Bullet;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.game.item.impl.gun.type.GunDamageType;
import in.prismar.library.common.random.UniqueRandomizer;
import in.prismar.library.spigot.entity.EntityUtil;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.particle.ParticleUtil;
import in.prismar.library.spigot.raytrace.result.RaytraceBlockHit;
import in.prismar.library.spigot.raytrace.result.RaytraceEntityHit;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import in.prismar.library.spigot.vector.VectorUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.util.Util;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ConvoyEscort extends DamageableAI {

    private List<Location> randomPatrolLocations;

    private ConvoyEscortState state = ConvoyEscortState.PATROLLING;

    private BukkitTask task;

    private int patrolTimePerSeconds = 8;

    private double findEntityRange = 40;

    private double maxFollowDistance = 40 * 40;

    private double startShootingDistance = 25 * 25;


    private long nextPatrolTimestamp;

    private LivingEntity followTarget;

    private ConvoyEscortGun gun;

    private final ConvoyEscortType type = ConvoyEscortType.FRIENDLY;

    public ConvoyEscort(Game game, Location startLocation) {
        super(game, "§cConvoy Escort", startLocation);
        Equipment equipment = getEquipment();
        equipment.set(Equipment.EquipmentSlot.HAND, new ItemBuilder(Material.WOODEN_PICKAXE).setCustomModelData(1).build());
        equipment.set(Equipment.EquipmentSlot.HELMET, new ItemBuilder(Material.IRON_HELMET).build());
        equipment.set(Equipment.EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.IRON_CHESTPLATE).build());
        equipment.set(Equipment.EquipmentSlot.LEGGINGS, new ItemBuilder(Material.IRON_LEGGINGS).build());
        equipment.set(Equipment.EquipmentSlot.BOOTS, new ItemBuilder(Material.IRON_BOOTS).build());
        setSkin("Soldier", "CrAO6t7pib6ebJ0defIziWhuUzRSLfzgb1gVI3gGJEELiVgu2klGgSQOR9YT4NruW19cnK8fs0Jszd/eq5X4WD0BlLX3RATdfg9m8t0RULB1I/ijVJx3FZo38FqnTER9qK6FLGXNa7Wo0dHtEz1EGJQAFry6vHMThnN5/oAY77yraaFkA7GBDaYNZUQ6U0/seoyYPwmDjXZz+J8twvrrKocYQZ4FYXKGc874xvKUxv3R98JzQ1VApUpZbrmFR4LBwjF1Vki4CFFlomu0hQviyw2DewaQllYzsqw8lVSX9ccFpu6ZxZEODoSYKHHhUgpMpk0DjKnckJwWbml1pPsOCQhlQ7/4+4Na7xiAB+/u/+9uj/XjIwird9Ql5Y3FPJz4Zr4zCtfTGMmqRGjkUjhnw9UDYb/G5QgzgOB1bM+kF7YDhdLf9Bb/gVSPAMtki9kY7QkyxD085Ue6+8yHYEnjS01kPBKhZAQ5SIWD+bsAtKo/GwiCg53hNjOO75RhIJjVyd7S4z0Sva1tk6/PL6zr8AjBECJetorQUVvh1iPQx7qU3o/ApwMDaa0n3xoSCGAygN62fxULkROyNHDznouOI0S0oY3FvyfIPfCupa5v+BVksb+thMJtFCcFqwf2o2cKjDx6O67tp+Et+1ozG6x3xCdX/ndd1ZT7o3guhd0isr0=", "ewogICJ0aW1lc3RhbXAiIDogMTcwNTMzNjU3ODg2MSwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS81ZTkxMjhkMDZjYjg5YTNkMDZiYjc5ZDJmNjg2OTYwYzljNDlmZjA5NDM5Nzk2NWU4NTRmNjA0ZGFkOGM5M2NmIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
        createRandomPatrolLocations();

        npc.setProtected(false);
        spawn();

        setMaxHealth(100);


        this.gun = new ConvoyEscortGun(game, npc);

        this.nextPatrolTimestamp = System.currentTimeMillis() + 1000L * patrolTimePerSeconds;

        this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(game, new Runnable() {
            long ticks = 0;

            @Override
            public void run() {
                if (ticks >= Integer.MAX_VALUE) {
                    ticks = 0; //Just in case it happens
                }
                if(asLivingEntity() == null) {
                    delete();
                    task.cancel();
                    return;
                }
                ticks++;
                //Check per second
                if (ticks % 10 == 0) {
                    if (state == ConvoyEscortState.PATROLLING) {
                        if (System.currentTimeMillis() >= nextPatrolTimestamp) {
                            nextPatrolTimestamp = System.currentTimeMillis() + 1000L * patrolTimePerSeconds;
                            Bukkit.getScheduler().runTask(game, () -> {
                                npc.getNavigator().setTarget(UniqueRandomizer.getRandom(npc.getUniqueId().toString(), randomPatrolLocations));
                            });
                        }
                        Bukkit.getScheduler().runTask(game, () -> {
                            LivingEntity entity = (LivingEntity) EntityUtil.findNearestEntity(npc.getEntity().getLocation(), findEntityRange, find -> {
                                if(find instanceof LivingEntity livingEntity) {
                                    if(livingEntity.isDead()) {
                                        return false;
                                    }
                                    if(type == ConvoyEscortType.FRIENDLY) {
                                        if(livingEntity instanceof Player) {
                                            return false;
                                        }
                                    }
                                    if(livingEntity instanceof Zombie) {
                                        return true;
                                    }
                                    if(CitizensAPI.getNPCRegistry().isNPC(livingEntity)) {
                                        return false;
                                    }
                                    return livingEntity instanceof Monster || livingEntity instanceof Player;
                                }
                                return false;
                            });
                            if(entity != null) {
                                followTarget = entity;
                                followTarget();
                            }
                        });

                    } else if(state == ConvoyEscortState.FOLLOWING) {
                        if(!npc.getEntity().getWorld().equals(followTarget.getWorld())) {
                            lostTarget();
                            return;
                        }
                        double distanceSquared = npc.getEntity().getLocation().distanceSquared(followTarget.getLocation());
                        if(distanceSquared > maxFollowDistance) {
                            lostTarget();
                            return;
                        }
                        if(distanceSquared < startShootingDistance) {
                            startShooting();
                            return;
                        }
                    }

                }
                //Check per 5 ticks
                if (ticks % 3 == 0) {
                    if(state == ConvoyEscortState.SHOOTING) {
                        if(!npc.getEntity().getWorld().equals(followTarget.getWorld())) {
                            lostTarget();
                            return;
                        }
                        if(followTarget.isDead()) {
                            lostTarget();
                            return;
                        }
                        double distanceSquared = npc.getEntity().getLocation().distanceSquared(followTarget.getLocation());
                        if(distanceSquared > maxFollowDistance) {
                            lostTarget();
                            return;
                        }
                        if(distanceSquared >= startShootingDistance + (2 * 2)) {
                            followTarget();
                            return;
                        }
                        Util.faceEntity(npc.getEntity(), followTarget);
                        //Shoot
                        Bukkit.getScheduler().runTask(getGame(), () -> {
                            gun.shoot();
                        });

                    }
                }

                //Check every tick

            }
        }, 1, 1);
    }

    private void startShooting() {
        state = ConvoyEscortState.SHOOTING;
        Bukkit.getScheduler().runTask(getGame(), () -> {
            npc.getNavigator().cancelNavigation();
            setSneaking(true);
        });

    }

    private void followTarget() {
        Bukkit.getScheduler().runTask(getGame(), () -> {
            npc.getNavigator().setTarget(followTarget, false);
            setSneaking(false);
        });
        state = ConvoyEscortState.FOLLOWING;

    }

    private void lostTarget() {
        state = ConvoyEscortState.PATROLLING;
        followTarget = null;
        nextPatrolTimestamp = 0;
        Bukkit.getScheduler().runTask(getGame(), () -> {
            setSneaking(false);
        });
    }

    private void createRandomPatrolLocations() {
        this.randomPatrolLocations = new ArrayList<>();
        for (int i = -8; i < 8; i++) {
            for (int j = -8; j < 8; j++) {
                //Find y
                Location cloneable = startLocation.clone().add(0, 10, 0);
                Location nextY = cloneable.clone();
                for (int k = 0; k < 20; k++) {
                    nextY = cloneable.clone().subtract(0, k, 0);
                    if (nextY.getBlock().getType().isSolid()) {
                        break;
                    }
                }
                Location patrol = startLocation.clone().add(i, 0, j);
                patrol.setY(nextY.getY());
                this.randomPatrolLocations.add(patrol);
            }
        }
    }

    public enum ConvoyEscortType {
        HOSTILE,
        FRIENDLY
    }

    public enum ConvoyEscortState {
        PATROLLING,
        FOLLOWING,

        SHOOTING
    }

    public class ConvoyEscortGun {

        private static final Vector DEFAULT_EYE_ROTATION = new Vector(-0.5, -0.5, 0.5);

        private final double sneakSpread = 6;
        private final double range = 40;

        private final double legDamage = 7;
        private final double bodyDamage = 8;
        private final double headDamage = 10;

        private final int maxAmmo = 30;

        private final long reloadTimeInTicks = 40;
        private final Game game;

        private int ammo;

        private NPC npc;

        private boolean isReloading;

        public ConvoyEscortGun(Game game, NPC npc) {
            this.game = game;
            ammo = maxAmmo;

            this.npc = npc;
        }

        private double getDamage(int damageReducePercentage, GunDamageType damageType) {
            double damage = 0;
            if (damageType == GunDamageType.LEGS) {
                damage = legDamage;
            } else if (damageType == GunDamageType.BODY) {
                damage = bodyDamage;
            } else if (damageType == GunDamageType.HEADSHOT) {
                damage = headDamage;
            }
            if (damageReducePercentage > 0) {
                damage -= (damage / 100) * damageReducePercentage;
            }
            return damage;
        }

        public void reload() {
            if(isReloading) {
                return;
            }
            isReloading = true;
            npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), "reload.ar.clipout", 1f, 1f);
            setSneaking(false);
            Bukkit.getScheduler().runTaskLaterAsynchronously(getGame(), () -> {
                if(asLivingEntity() == null) {
                    return;
                }
                ammo = maxAmmo;
                isReloading = false;
                npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), "reload.ar.clipin", 1f, 1f);
                if(state == ConvoyEscortState.SHOOTING) {
                    Bukkit.getScheduler().runTask(getGame(), () -> {
                        setSneaking(true);
                    });
                }
            }, reloadTimeInTicks);

        }

        public void shoot() {
            if(npc.getEntity() == null) {
                return;
            }
            if(ammo <= 0) {
                reload();
                return;
            }
            npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), "shoot.ak", 2.5f, 1f);
            ammo--;
            double spread = sneakSpread;
            LivingEntity livingEntity = (LivingEntity) npc.getEntity();
            Location eyeLocation = livingEntity.getEyeLocation();
            Location particleOrigin = eyeLocation.clone();
            Vector eyeOffset = VectorUtil.rotateVector(DEFAULT_EYE_ROTATION, eyeLocation.getYaw(), eyeLocation.getPitch());
            particleOrigin = particleOrigin.add(eyeOffset);
            particleOrigin = particleOrigin.add(eyeLocation.getDirection().normalize().multiply(2));
            Bullet bullet = new Bullet(particleOrigin, eyeLocation.clone(),
                    VectorUtil.getRandomizedDirection(npc.getEntity(), spread), range);
            List<RaytraceHit> hits = bullet.invoke();
            int damageReducePercentage = 0;
            for (RaytraceHit hit : hits) {
                if (hit instanceof RaytraceEntityHit entityHit) {
                    if (!entityHit.getTarget().getUniqueId().equals(npc.getEntity().getUniqueId())) {
                        if (entityHit.getTarget() instanceof Player targetPlayer) {
                            GunPlayer target = GunPlayer.of(targetPlayer);
                            if (target.isShielded()) {
                                double angle = getAngleBetweenPoints(targetPlayer.getLocation(), npc.getEntity().getLocation());
                                if (angle <= 1.1) {
                                    targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1f, 1);
                                    spawnParticle(particleOrigin, entityHit.getPoint());
                                    return;
                                }
                            }
                        }

                        GunDamageType damageType = getHitType(entityHit.getTarget(), entityHit.getPoint());
                        double damage = getDamage(damageReducePercentage, damageType);
                        if (damage > 0) {
                            LivingEntity entity = (LivingEntity) entityHit.getTarget();
                            if (entity instanceof Player target) {
                                GunPlayer targetGunPlayer = GunPlayer.of(target);
                                targetGunPlayer.setLastDamageReceived(damageType);
                            }
                            entity.setNoDamageTicks(0);
                            entity.damage(damage, npc.getEntity());
                            entity.setVelocity(new Vector());
                        }
                        damageReducePercentage += 30;
                    }
                } else if (hit instanceof RaytraceBlockHit blockHit) {
                    if (blockHit.getTarget().getType().name().contains("GLASS")) {
                        blockHit.getPoint().getWorld().playSound(blockHit.getPoint(), "impact.glass", 0.35f, 1);
                    } else if (blockHit.getTarget().getType().name().contains("LOG") || blockHit.getTarget().getType().name().contains("PLANKS")) {
                        blockHit.getPoint().getWorld().playSound(blockHit.getPoint(), "impact.tree", 0.35f, 1);
                    }
                    spawnParticle(particleOrigin, blockHit.getPoint());
                    return;
                }
            }
            spawnParticle(particleOrigin, bullet.getEndPoint());
        }
    }



    private double getAngleBetweenPoints(Location source, Location target) {
        Vector inBetween = target.clone().subtract(source).toVector();
        Vector forward = source.getDirection();
        return forward.angle(inBetween);
    }

    private void spawnParticle(Location particleOrigin, Location location) {
        //TODO: muzzle flash particleOrigin.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleOrigin, 0);
        ParticleUtil.spawnParticleAlongLine(particleOrigin, location, Particle.CRIT, 20, 0);
    }

    private GunDamageType getHitType(Entity target, Location hitPoint) {
        double distance = target.getLocation().distanceSquared(hitPoint);
        GunDamageType damageType = GunDamageType.BODY;
        if (distance <= 0.7) {
            damageType = GunDamageType.LEGS;
        } else if (distance > 0.7 && distance <= 2.1) {
            damageType = GunDamageType.BODY;
        } else if (distance >= 2.1) {
            damageType = GunDamageType.HEADSHOT;
        }
        return damageType;
    }


}
