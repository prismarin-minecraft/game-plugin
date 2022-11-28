package in.prismar.game.item.impl.gun;

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
import in.prismar.library.spigot.raytrace.result.RaytraceBlockHit;
import in.prismar.library.spigot.raytrace.result.RaytraceEntityHit;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;

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

    /**
     * These maps can be potential memory leaks
     */
    private static Map<UUID, Long> LAST_INTERACT = new HashMap<>();
    private static Map<UUID, Long> RELOADING = new HashMap<>();
    private static Map<UUID, Long> UPDATE_PLAYER_TICK = new HashMap<>();

    public static Map<UUID, GunDamageType> LAST_DAMAGE = new HashMap<>();
    //----------------------------------------------------------------------------------


    private final GunType type;

    private Particle shootParticle = Particle.CRIT;
    private AmmoType ammoType = AmmoType.PISTOL;
    private double range = 20;
    private int fireRate = 120;

    private boolean rightClickShoot = true;
    private boolean disableInteraction = true;

    private double spread = 1;
    private double sneakSpread = 0.5;

    private int bulletsPerShot = 1;

    private int maxAmmo = 10;
    private int reloadTimeInTicks = 20;

    private double legDamage = 2;
    private double bodyDamage = 3;
    private double headDamage = 5;

    private int attachmentSlots = 3;

    private Map<GunSoundType, List<GunSound>> sounds;


    public Gun(String id, GunType type, Material material, String displayName) {
        super(id, material, displayName);
        this.sounds = new HashMap<>();
        this.type = type;

        registerSound(GunSoundType.SHOOT, Sound.ENTITY_BLAZE_HURT, 0.6f, 2f);

        registerSound(GunSoundType.HIT, Sound.ENTITY_PLAYER_ATTACK_CRIT, 0.9f, 1.66f);
        registerSound(GunSoundType.HEADSHOT, Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.3f, 1f);

        registerSound(GunSoundType.RELOAD, Sound.BLOCK_PISTON_CONTRACT, 0.65f, 0.7f);

        setUnbreakable(true);

        allFlags();

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
        for(Attachment attachment : attachments) {
            lore.add("   §8➥ §b" + attachment.getDisplayName());
        }
        lore.add("§c ");
        return lore;
    }

    public void playSound(Player player, GunSoundType type) {
        if (sounds.containsKey(type)) {
            for (GunSound sound : sounds.get(type)) {
                if (type.isSurrounding()) {
                    player.getWorld().playSound(player.getLocation(), sound.getSound(), sound.getVolume(), sound.getPitch());
                } else {
                    player.playSound(player.getLocation(), sound.getSound(), sound.getVolume(), sound.getPitch());
                }
            }

        }

    }

    public void registerSound(GunSoundType type, Sound sound, float volume, float pitch) {
        if (!this.sounds.containsKey(type)) {
            this.sounds.put(type, new ArrayList<>());
        }
        this.sounds.get(type).add(new GunSound(sound, volume, pitch));
    }

    public void clearSounds(GunSoundType type) {
        if(this.sounds.containsKey(type)) {
            this.sounds.get(type).clear();
        }
    }

    public List<Attachment> getAttachments(Game game, ItemStack stack) {
        List<Attachment> attachments = new ArrayList<>();
        final String value = PersistentItemDataUtil.getString(game, stack, ATTACHMENTS_KEY);
        for(String id : value.split(",")) {
            CustomItem item = game.getItemRegistry().getItemById(id);
            if(item != null) {
                if(item instanceof Attachment attachment) {
                    attachments.add(attachment);
                }
            }
        }
        return attachments;
    }


    public void shoot(Game game, Player player, ItemStack stack) {
        double normalSpread = this.spread;
        double sneakSpread = this.sneakSpread;
        double range = this.range;
        boolean allowParticle = true;
        for(Attachment attachment : getAttachments(game, stack)) {
            normalSpread = attachment.apply(AttachmentModifier.SPREAD, normalSpread);
            sneakSpread = attachment.apply(AttachmentModifier.SNEAK_SPREAD, sneakSpread);
            range = attachment.apply(AttachmentModifier.RANGE, range);
            allowParticle = attachment.apply(AttachmentModifier.PARTICLE, allowParticle);
        }
        double spread = !player.isSneaking() ? normalSpread : sneakSpread;
        Bullet bullet = new Bullet(allowParticle ? shootParticle : null, player.getEyeLocation().subtract(0, 0.0, 0),
                getRandomizedDirection(player, spread), range);
        playSound(player, GunSoundType.SHOOT);
        List<RaytraceHit> hits = bullet.invoke();
        int damageReduce = 0;
        for(RaytraceHit hit : hits) {
            if(hit instanceof RaytraceEntityHit entityHit) {
                if(!entityHit.getTarget().getName().equals(player.getName())) {
                    double distance = entityHit.getTarget().getLocation().distanceSquared(entityHit.getPoint());
                    double damage = 0;
                    GunDamageType damageType = GunDamageType.BODY;
                    if(distance <= 0.7) {
                        damageType = GunDamageType.LEGS;
                        damage = legDamage;
                    } else if(distance > 0.7 && distance <= 2.1) {
                        damage = bodyDamage;
                        damageType = GunDamageType.BODY;
                    } else if (distance >= 2.1) {
                        damage = headDamage;
                        damageType = GunDamageType.HEADSHOT;
                    }
                    for (int i = 0; i < damageReduce; i++) {
                        damage -= (damage / 100) * 50;
                    }
                    if(damage > 0) {
                        LivingEntity entity = (LivingEntity)entityHit.getTarget();
                        LAST_DAMAGE.put(entity.getUniqueId(), damageType);
                        entity.damage(damage, player);
                        if(damageType == GunDamageType.HEADSHOT) {
                            playSound(player, GunSoundType.HEADSHOT);
                        } else {
                            playSound(player, GunSoundType.HIT);
                        }
                    }
                    damageReduce++;
                }
            } else if(hit instanceof RaytraceBlockHit blockHit) {
                damageReduce++;
            }
        }
    }

    public void reload(Player player, Game game, ItemStack stack) {
        if(RELOADING.containsKey(player.getUniqueId())) {
            return;
        }
        List<Attachment> attachments = getAttachments(game, stack);
        int maxAmmo = this.maxAmmo;
        for(Attachment attachment : attachments) {
            maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
        }

        int current = PersistentItemDataUtil.getInteger(game, stack, AMMO_KEY);
        if(current >= maxAmmo) {
            return;
        }
        int needed = maxAmmo - current;

        int ammoToGive;

        int ammo = AmmoType.getAmmoInInventory(player, ammoType);
        if(ammo >= 1) {
            if(ammo < needed) {
                AmmoType.takeAmmo(player, ammoType, ammo);
                ammoToGive = current + ammo;
            } else {
                AmmoType.takeAmmo(player, ammoType, needed);
                ammoToGive = maxAmmo;
            }
            int finalAmmoToGive = ammoToGive;
            Bukkit.getScheduler().runTaskLater(game, () -> {
                PersistentItemDataUtil.setInteger(game, stack, AMMO_KEY, finalAmmoToGive);
                RELOADING.remove(player.getUniqueId());
            }, reloadTimeInTicks);
            RELOADING.put(player.getUniqueId(), System.currentTimeMillis() + 50 * reloadTimeInTicks);
            playSound(player, GunSoundType.RELOAD);
        }



    }

    @CustomItemEvent
    public void onUpdate(Player player, Game game, CustomItemHolder holder, CustomItemHolder event) {
        if(event.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        if(!UPDATE_PLAYER_TICK.containsKey(player.getUniqueId())) {
            UPDATE_PLAYER_TICK.put(player.getUniqueId(), 0l);
        }
        long currentUpdateTick = UPDATE_PLAYER_TICK.get(player.getUniqueId());
        if(currentUpdateTick >= Integer.MAX_VALUE) {
            currentUpdateTick = 0;
        }
        int ammo = PersistentItemDataUtil.getInteger(game, holder.getStack(), AMMO_KEY);
        List<Attachment> attachments = getAttachments(game, holder.getStack());
        int fireRate = this.fireRate;
        int maxAmmo = this.maxAmmo;
        for(Attachment attachment : attachments) {
            fireRate = attachment.apply(AttachmentModifier.FIRE_RATE, fireRate);
            maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
        }
        if(RELOADING.containsKey(player.getUniqueId())) {
            long max = RELOADING.get(player.getUniqueId());
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
        long lastInteract = LAST_INTERACT.containsKey(player.getUniqueId()) ? LAST_INTERACT.get(player.getUniqueId()) : 0;
        long difference = System.currentTimeMillis() - lastInteract;
        if (difference <= 210 && currentUpdateTick % fireRateTicks == 0) {
            if(ammo >= 1) {
                for (int i = 0; i < bulletsPerShot; i++) {
                    shoot(game, player, holder.getStack());
                }
                ammo--;
                PersistentItemDataUtil.setInteger(game, holder.getStack(), AMMO_KEY, ammo);
            }
        }
        if(difference > 210) {
            LAST_INTERACT.remove(player.getUniqueId());
        }
        currentUpdateTick++;
        UPDATE_PLAYER_TICK.put(player.getUniqueId(), currentUpdateTick);

    }

    @CustomItemEvent
    public void onInteract(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() == CustomItemHoldingType.RIGHT_HAND) {
            boolean allowedInteract = rightClickShoot ? event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR :
                    event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR;
            if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(!rightClickShoot) {
                    updateLastInteract(player);
                    int ammo = PersistentItemDataUtil.getInteger(game, holder.getStack(), AMMO_KEY);
                    if(ammo <= 0) {
                        reload(event.getPlayer(), game, holder.getStack());
                    }
                } else {
                    reload(event.getPlayer(), game, holder.getStack());
                }

            } else if(allowedInteract) {
                if(disableInteraction) {
                    event.setCancelled(true);
                }
               updateLastInteract(player);
            }

        }
    }

    private void updateLastInteract(Player player) {
        if(!LAST_INTERACT.containsKey(player.getUniqueId())) {
            UPDATE_PLAYER_TICK.put(player.getUniqueId(), 0l);
        }
        LAST_INTERACT.put(player.getUniqueId(), System.currentTimeMillis());
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
