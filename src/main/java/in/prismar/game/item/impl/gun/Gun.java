package in.prismar.game.item.impl.gun;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.data.SeasonData;
import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunDamageType;
import in.prismar.game.item.impl.gun.type.GunType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import in.prismar.library.spigot.particle.ParticleUtil;
import in.prismar.library.spigot.raytrace.result.RaytraceBlockHit;
import in.prismar.library.spigot.raytrace.result.RaytraceEntityHit;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Gun extends CustomItem {

    //----------------------------------------------------------------------------------
    public static final String AMMO_KEY = "ammo";
    public static final String ATTACHMENTS_KEY = "attachments";
    private static final DecimalFormat RELOADING_DECIMAL_FORMAT = new DecimalFormat("0.0");

    //----------------------------------------------------------------------------------


    private final GunType type;

    private Particle shootParticle = Particle.CRIT;
    private AmmoType ammoType = AmmoType.PISTOL;

    private Map<Material, Integer> wallbangTypes;
    private double range = 20;
    private int fireRate = 120;

    private boolean disableInteraction = true;

    private double spread = 1;
    private double sneakSpread = 0.5;

    private int bulletsPerShot = 1;

    private int maxAmmo = 10;
    private boolean unlimitedAmmo;
    private int reloadTimeInTicks = 20;

    private double legDamage = 2;
    private double bodyDamage = 3;
    private double headDamage = 5;

    private int attachmentSlots = 3;

    private int zoom;

    private Map<GunSoundType, GunSound> sounds;


    public Gun(String id, GunType type, Material material, String displayName) {
        super(id, material, displayName);
        this.sounds = new HashMap<>();
        this.type = type;
        this.wallbangTypes = new HashMap<>();

        for(Material wallbangType : Material.values()) {
            if(wallbangType.name().contains("WOOD") || wallbangType.name().contains("LOG")) {
                addWallbangTypes(wallbangType, 25);
            } else if(wallbangType.name().contains("GLASS")) {
                addWallbangTypes(wallbangType, 0);
            }
        }

        registerSound(GunSoundType.SHOOT, Sound.ENTITY_BLAZE_HURT, 0.6f, 1f);

        registerSound(GunSoundType.HIT, Sound.BLOCK_BASALT_HIT, 0.9f, 2F);
        registerSound(GunSoundType.HEADSHOT, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.35f, 1f);

        registerSound(GunSoundType.RELOAD, Sound.BLOCK_PISTON_CONTRACT, 0.65f, 0.7f);

        GunSound impactSound = registerSound(GunSoundType.BULLET_IMPACT, Sound.BLOCK_STONE_HIT, 0.65f, 1);
        impactSound.setSurroundingDistance(10);

        setUnbreakable(true);

        allFlags();

    }

    public Gun addWallbangTypes(Material material, int reduce) {
        this.wallbangTypes.put(material, reduce);
        return this;
    }

    public void generateDefaultLore() {
        setLore(buildDefaultLore(Collections.emptyList()));
    }

    public List<String> buildDefaultLore(List<Attachment> attachments) {
        List<String> lore = new ArrayList<>();
        lore.add("§c ");
        lore.add(" §7Type§8: §b" + type.getDisplayName());
        lore.add(" §7Range§8: §b" + range);
        lore.add(" §7RPM§8: §b" + fireRate);
        lore.add(" §7Spread§8: §b" + spread);
        lore.add(" §7Sneak Spread§8: §b" + sneakSpread);
        lore.add(" §7Magazine§8: §b" + maxAmmo);
        lore.add(" §7Reload time§8: §b" + reloadTimeInTicks / 20 + "s");
        lore.add(" §7Damage§8: §b");
        lore.add("   §8➥ §7Head§8: §b" + headDamage);
        lore.add("   §8➥ §7Body§8: §b" + bodyDamage);
        lore.add("   §8➥ §7Legs§8: §b" + legDamage);
        lore.add(" §7Attachments §8(§3" + attachments.size() + "§8/§3" + getAttachmentSlots() + "§8)");
        for (Attachment attachment : attachments) {
            lore.add("   §8➥ §b" + attachment.getDisplayName());
        }
        lore.add("§c ");
        return lore;
    }

    public void playSound(Player player, GunSoundType type) {
        playSound(player, player.getLocation(), type);
    }

    public void playSound(Player player, Location location, GunSoundType type) {
        if (sounds.containsKey(type)) {
            GunSound sound = sounds.get(type);
            if (type.isSurrounding()) {
                for(Player near : player.getWorld().getPlayers()) {
                    if(near.getLocation().distanceSquared(location) <= (sound.getSurroundingDistance() * sound.getSurroundingDistance())) {
                        if(sound.getSound() == null) {
                            near.playSound(location, sound.getSoundName(), sound.getVolume(), sound.getPitch());
                        } else {
                            near.playSound(location, sound.getSound(), sound.getVolume(), sound.getPitch());
                        }
                    }
                }
            } else {
                if(sound.getSound() == null) {
                    player.playSound(location, sound.getSoundName(), sound.getVolume(), sound.getPitch());
                } else {
                    player.playSound(location, sound.getSound(), sound.getVolume(), sound.getPitch());
                }

            }
        }
    }

    public GunSound registerSound(GunSoundType type, Sound sound, float volume, float pitch) {
        GunSound gunSound = new GunSound(sound, volume, pitch);
        this.sounds.put(type, gunSound);
        return gunSound;
    }

    public GunSound registerSound(GunSoundType type, String sound, float volume, float pitch) {
        GunSound gunSound = new GunSound(null, volume, pitch);
        gunSound.setSoundName(sound);
        this.sounds.put(type, gunSound);
        return gunSound;
    }



    public List<Attachment> getAttachments(Game game, ItemStack stack) {
        List<Attachment> attachments = new ArrayList<>();
        final String value = PersistentItemDataUtil.getString(game, stack, ATTACHMENTS_KEY);
        for (String id : value.split(",")) {
            CustomItem item = game.getItemRegistry().getItemById(id);
            if (item != null) {
                if (item instanceof Attachment attachment) {
                    attachments.add(attachment);
                }
            }
        }
        return attachments;
    }

    private long addStatsValue(SeasonData seasonData, String key) {
        long current = seasonData.getStats().getOrDefault(key, 0l) + 1;
        seasonData.getStats().put(key, current);
        return current;
    }


    public void shoot(Game game, Player player, ItemStack stack) {
        GunPlayer gunPlayer = GunPlayer.of(player);
        SeasonData seasonData = gunPlayer.getUser().getSeasonData();
       addStatsValue(seasonData, "shots");

        double normalSpread = this.spread;
        double sneakSpread = this.sneakSpread;
        double range = this.range;
        boolean allowParticle = true;
        for (Attachment attachment : getAttachments(game, stack)) {
            normalSpread = attachment.apply(AttachmentModifier.SPREAD, normalSpread);
            sneakSpread = attachment.apply(AttachmentModifier.SNEAK_SPREAD, sneakSpread);
            range = attachment.apply(AttachmentModifier.RANGE, range);
            allowParticle = attachment.apply(AttachmentModifier.PARTICLE, allowParticle);
        }
        double spread = !player.isSneaking() ? normalSpread : sneakSpread;
        Location eyeLocation = player.getEyeLocation();

        double distanceFromEyes = -0.5;

        double distanceFromEyeCenter = 0.5;
        Vector eyeOffset = rotateVector(new Vector(distanceFromEyes, -0.5, distanceFromEyeCenter), eyeLocation.getYaw(), eyeLocation.getPitch());
        Location particleOrigin = eyeLocation.clone().add(eyeOffset);

        Bullet bullet = new Bullet(particleOrigin, eyeLocation.clone(),
                getRandomizedDirection(player, spread), range);
        playSound(player, GunSoundType.SHOOT);
        List<RaytraceHit> hits = bullet.invoke();
        int damageReducePercentage = 0;
        for (RaytraceHit hit : hits) {
            if (hit instanceof RaytraceEntityHit entityHit) {
                if (!entityHit.getTarget().getName().equals(player.getName())) {
                    double damage = 0;
                    GunDamageType damageType = getHitType(entityHit.getTarget(), entityHit.getPoint());
                    if (damageType == GunDamageType.LEGS) {
                        damageType = GunDamageType.LEGS;
                        damage = legDamage;
                    } else if (damageType == GunDamageType.BODY) {
                        damage = bodyDamage;
                        damageType = GunDamageType.BODY;
                    } else if (damageType == GunDamageType.HEADSHOT) {
                        damage = headDamage;
                    }
                    if(damageReducePercentage > 0) {
                        damage -= (damage / 100) * damageReducePercentage;
                    }

                    if (damage > 0) {
                        LivingEntity entity = (LivingEntity) entityHit.getTarget();
                        if(entity instanceof Player target) {
                            GunPlayer targetGunPlayer = GunPlayer.of(target);
                            targetGunPlayer.setLastDamageReceived(damageType);
                        }
                        entity.damage(damage, player);
                        if (damageType == GunDamageType.HEADSHOT) {
                            playSound(player, GunSoundType.HEADSHOT);
                            addStatsValue(seasonData, "headshots");
                        } else {
                            playSound(player, GunSoundType.HIT);
                        }
                    }
                    addStatsValue(seasonData, "hits");
                    damageReducePercentage += 30;
                }
            } else if (hit instanceof RaytraceBlockHit blockHit) {
                if(!wallbangTypes.containsKey(blockHit.getTarget().getType())) {
                    blockHit.getTarget().getWorld().spawnParticle(Particle.BLOCK_DUST, blockHit.getPoint(), 2,
                            blockHit.getTarget().getBlockData());
                    playSound(player, blockHit.getPoint(), GunSoundType.BULLET_IMPACT);
                    if(allowParticle) {
                        spawnParticle(particleOrigin, blockHit.getPoint());
                    }
                    return;
                }
                damageReducePercentage += wallbangTypes.get(blockHit.getTarget().getType());
            }
        }
        if(allowParticle) {
            spawnParticle(particleOrigin, bullet.getEndPoint());
        }
    }

    private void spawnParticle(Location particleOrigin, Location location) {
        ParticleUtil.spawnParticleAlongLine(particleOrigin, location, shootParticle, 20, 0);
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

    private final Vector rotateVector(Vector v, float yawDegrees, float pitchDegrees) {
        double yaw = Math.toRadians(-1 * (yawDegrees + 90));
        double pitch = Math.toRadians(-pitchDegrees);

        double cosYaw = Math.cos(yaw);
        double cosPitch = Math.cos(pitch);
        double sinYaw = Math.sin(yaw);
        double sinPitch = Math.sin(pitch);

        double initialX, initialY, initialZ;
        double x, y, z;

        // Z_Axis rotation (Pitch)
        initialX = v.getX();
        initialY = v.getY();
        x = initialX * cosPitch - initialY * sinPitch;
        y = initialX * sinPitch + initialY * cosPitch;

        // Y_Axis rotation (Yaw)
        initialZ = v.getZ();
        initialX = x;
        z = initialZ * cosYaw - initialX * sinYaw;
        x = initialZ * sinYaw + initialX * cosYaw;

        return new Vector(x, y, z);
    }

    public void reload(Player player, Game game, ItemStack stack) {
        GunPlayer gunPlayer = GunPlayer.of(player);
        if (gunPlayer.isReloading()) {
            return;
        }
        List<Attachment> attachments = getAttachments(game, stack);
        int maxAmmo = this.maxAmmo;
        for (Attachment attachment : attachments) {
            maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
        }

        int current = PersistentItemDataUtil.getInteger(game, stack, AMMO_KEY);
        if (current >= maxAmmo) {
            return;
        }
        int needed = maxAmmo - current;

        int ammoToGive;

        int ammo = !unlimitedAmmo ? AmmoType.getAmmoInInventory(player, ammoType) : needed;
        if (ammo >= 1) {
            if (ammo < needed) {
                AmmoType.takeAmmo(player, ammoType, ammo);
                ammoToGive = current + ammo;
            } else {
                AmmoType.takeAmmo(player, ammoType, needed);
                ammoToGive = maxAmmo;
            }
            int finalAmmoToGive = ammoToGive;
            Bukkit.getScheduler().runTaskLater(game, () -> {
                if(!gunPlayer.isReloading()) {
                    return;
                }
                PersistentItemDataUtil.setInteger(game, stack, AMMO_KEY, finalAmmoToGive);
                gunPlayer.setReloading(false);
            }, reloadTimeInTicks);
            gunPlayer.setReloading(true);
            gunPlayer.setReloadingGunId(getId());
            gunPlayer.setReloadingEndTimestamp(System.currentTimeMillis() + 50 * reloadTimeInTicks);
            playSound(player, GunSoundType.RELOAD);
        }


    }

    @CustomItemEvent
    public void onUpdate(Player player, Game game, CustomItemHolder holder, CustomItemHolder event) {
        if (event.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        GunPlayer gunPlayer = GunPlayer.of(player);

        if(zoom > 0) {
            if(player.isSneaking()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, zoom - 1));
            } else {
                player.removePotionEffect(PotionEffectType.SLOW);
            }
        }


        long currentUpdateTick = gunPlayer.getCurrentUpdateTick();
        if (currentUpdateTick >= Integer.MAX_VALUE) {
            currentUpdateTick = 0;
        }
        int ammo = PersistentItemDataUtil.getInteger(game, holder.getStack(), AMMO_KEY);
        List<Attachment> attachments = getAttachments(game, holder.getStack());
        int fireRate = this.fireRate;
        int maxAmmo = this.maxAmmo;
        for (Attachment attachment : attachments) {
            fireRate = attachment.apply(AttachmentModifier.FIRE_RATE, fireRate);
            maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
        }
        if (gunPlayer.isReloading()) {
            if(!gunPlayer.getReloadingGunId().equals(getId())) {
                gunPlayer.setReloading(false);
                return;
            }
            long max = gunPlayer.getReloadingEndTimestamp();
            long difference = max - System.currentTimeMillis();
            double result = (double) difference / 1000.0;
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§eReloading " + RELOADING_DECIMAL_FORMAT.format(result) + "s"));
            return;
        } else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ammo <= 0 ? "§cNo ammo" : "§b" + ammo + "§8/§7" + maxAmmo
            ));
        }

        int fireRateTicks = 1200 / fireRate;
        long lastInteract = gunPlayer.getLastInteract();
        long difference = System.currentTimeMillis() - lastInteract;
        if (difference <= 210 && currentUpdateTick >= fireRateTicks) {
            if (ammo >= 1) {
                for (int i = 0; i < bulletsPerShot; i++) {
                    shoot(game, player, holder.getStack());
                }
                ammo--;
                PersistentItemDataUtil.setInteger(game, holder.getStack(), AMMO_KEY, ammo);
                currentUpdateTick = 0;
            }
        }
        currentUpdateTick++;
        gunPlayer.setCurrentUpdateTick(currentUpdateTick);
    }

    @CustomItemEvent
    public void onInteract(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() == CustomItemHoldingType.RIGHT_HAND) {
            boolean allowedInteract = event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR;
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                reload(event.getPlayer(), game, holder.getStack());
            } else if (allowedInteract) {
                if(game.getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp")) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not allowed to use this item inside a safe region.");
                    return;
                }
                if (disableInteraction) {
                    event.setCancelled(true);
                }
                GunPlayer gunPlayer = GunPlayer.of(player);
                gunPlayer.setLastInteract(System.currentTimeMillis());
            }

        }
    }


    private Vector getRandomizedDirection(Player player, double spread) {
        Vector dir = player.getLocation().getDirection().normalize();
        dir.setX(dir.getX() + getRandFactor(spread));
        dir.setY(dir.getY() + getRandFactor(spread));
        dir.setZ(dir.getZ() + getRandFactor(spread));
        return dir;
    }

    private double getRandFactor(double spread) {
        return MathUtil.randomDouble(-0.01, 0.01) * spread;
    }

}
