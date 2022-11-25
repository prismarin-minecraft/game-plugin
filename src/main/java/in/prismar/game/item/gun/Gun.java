package in.prismar.game.item.gun;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.gun.sound.GunSound;
import in.prismar.game.item.gun.sound.GunSoundType;
import in.prismar.game.item.gun.type.AmmoType;
import in.prismar.game.item.gun.type.GunType;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.raytrace.result.RaytraceBlockHit;
import in.prismar.game.raytrace.result.RaytraceEntityHit;
import in.prismar.game.raytrace.result.RaytraceHit;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
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

    public static final String AMMO_KEY = "ammo";

    private static final DecimalFormat RELOADING_DECIMAL_FORMAT = new DecimalFormat("0.0");
    private static Map<UUID, Long> LAST_INTERACT = new HashMap<>();
    private static Map<UUID, Long> RELOADING = new HashMap<>();
    private static Map<UUID, Long> UPDATE_PLAYER_TICK = new HashMap<>();

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

    protected void generateDefaultLore() {
        addLore("§c ");
        addLore(" §7Type§8: §b" + type.getDisplayName());
        addLore(" §7Range§8: §b" + range);
        addLore(" §7RPM§8: §b" + fireRate);
        addLore(" §7Spread§8: §b" + spread);
        addLore(" §7Sneak Spread§8: §b" + sneakSpread);
        addLore(" §7Magazine§8: §b" + maxAmmo);
        addLore(" §7Reload time§8: §b" + reloadTimeInTicks / 20 + "s");
        addLore(" §7Damage§8: §b");
        addLore("   §8➥ §7Head§8: §b" + headDamage);
        addLore("   §8➥ §7Body§8: §b" + bodyDamage);
        addLore("   §8➥ §7Legs§8: §b" + legDamage);
        addLore("§c ");
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

    public void shoot(Player player) {
        double spread = !player.isSneaking() ? this.spread : this.sneakSpread;
        Bullet bullet = new Bullet(shootParticle, player.getEyeLocation().subtract(0, 0.3, 0),
                getRandomizedDirection(player, spread), range);
        playSound(player, GunSoundType.SHOOT);
        List<RaytraceHit> hits = bullet.invoke();
        int damageReduce = 0;
        for(RaytraceHit hit : hits) {
            if(hit instanceof RaytraceEntityHit entityHit) {
                if(!entityHit.getTarget().getName().equals(player.getName())) {
                    boolean headshot = false;
                    double distance = entityHit.getTarget().getLocation().distanceSquared(entityHit.getPoint());
                    double damage = 0;
                    if(distance <= 0.7) {
                        damage = legDamage;
                    } else if(distance > 0.7 && distance <= 2.1) {
                        damage = bodyDamage;
                    } else if (distance >= 2.1) {
                        damage = headDamage;
                        headshot = true;
                    }
                    for (int i = 0; i < damageReduce; i++) {
                        damage -= (damage / 100) * 50;
                    }
                    if(damage > 0) {
                        LivingEntity entity = (LivingEntity)entityHit.getTarget();
                        entity.damage(damage, player);
                        if(headshot) {
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
                    shoot(player);
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
