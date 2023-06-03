package in.prismar.game.item.impl.gun;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.data.SeasonData;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.event.bus.GunPreShootEvent;
import in.prismar.game.item.event.bus.GunReloadEvent;
import in.prismar.game.item.event.bus.GunShootEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.game.item.impl.gun.player.GunPlayerState;
import in.prismar.game.item.impl.gun.recoil.RecoilTask;
import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunDamageType;
import in.prismar.game.item.impl.gun.type.GunType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.model.SkinableItem;
import in.prismar.game.tracer.BulletTracer;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import in.prismar.library.spigot.particle.ParticleUtil;
import in.prismar.library.spigot.raytrace.result.RaytraceBlockHit;
import in.prismar.library.spigot.raytrace.result.RaytraceEntityHit;
import in.prismar.library.spigot.raytrace.result.RaytraceHit;
import in.prismar.library.spigot.vector.VectorUtil;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Gun extends SkinableItem {

    //----------------------------------------------------------------------------------
    public static final String AMMO_KEY = "ammo";
    public static final String ATTACHMENTS_KEY = "attachments";
    private static final DecimalFormat RELOADING_DECIMAL_FORMAT = new DecimalFormat("0.0");

    private static final Vector DEFAULT_EYE_ROTATION = new Vector(-0.5, -0.5, 0.5);

    public static final Cache<ItemStack, List<Attachment>> ATTACHMENT_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES).build();

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

    private double recoil = 0.35;

    private int attachmentSlots = 3;

    private boolean smallLore;

    private int zoom;

    private ItemStack aimItem;

    private ItemStack reloadItem;

    private ItemStack zoomItem;

    private String previewImage;

    private Map<GunSoundType, GunSound> sounds;

    private Map<Integer, ItemStack> skinnedStateItems;


    public Gun(String id, GunType type, Material material, String displayName) {
        super(id, material, displayName);
        this.sounds = new HashMap<>();
        this.type = type;
        this.wallbangTypes = new HashMap<>();
        this.skinnedStateItems = new HashMap<>();

        if (type == GunType.SNIPER) {
            this.zoomItem = new ItemBuilder(Material.PAPER).setCustomModelData(1).setName(displayName).build();
            registerSound(GunSoundType.AIM_IN, "aim.sniper", 0.8f, 1);
        } else {
            registerSound(GunSoundType.AIM_IN, "aim.in", 0.8f, 1);
            registerSound(GunSoundType.AIM_OUT, "aim.out", 0.8f, 1);
        }
        for (Material wallbangType : Material.values()) {
            if (wallbangType.name().contains("WOOD") || wallbangType.name().contains("LOG") || wallbangType.name().contains("PLANK")) {
                addWallbangTypes(wallbangType, 25);
            } else if (wallbangType.name().contains("GLASS") || wallbangType.name().contains("LEAVES")) {
                addWallbangTypes(wallbangType, 0);
            }
        }

        registerSound(GunSoundType.LOW_AMMO, "misc.lowammo", 0.8f, 1);
        registerSound(GunSoundType.HIT, "impact.hit", 0.9f, 1f);
        registerSound(GunSoundType.HEADSHOT, "impact.headshot", 0.5f, 1f);

        registerSound(GunSoundType.RELOAD_IN, "reload." + type.name().toLowerCase() + ".clipin", 1f, 1f);
        registerSound(GunSoundType.RELOAD_OUT, "reload." + type.name().toLowerCase() + ".clipout", 1f, 1f);

        registerSound(GunSoundType.SHOOT, "shoot." + type.name().toLowerCase(), 1f, 1f);

        registerSound(GunSoundType.EQUIP, "equip.guns." + type.name().toLowerCase(), 0.7f, 1f);


        setUnbreakable(true);

        allFlags();
    }

    public ItemStack getCurrentReloadItem(ItemStack stack) {
        int data = getSkinDataByItem(stack);
        if(data == -1) {
            return reloadItem;
        }
        int nextData = data + 1;
        if(!skinnedStateItems.containsKey(nextData)) {
            skinnedStateItems.put(nextData, new ItemBuilder(getMaterial()).setName(getDisplayName()).setCustomModelData(nextData).build());
        }
        return skinnedStateItems.get(nextData);
    }

    public ItemStack getCurrentAimItem(ItemStack stack) {
        int data = getSkinDataByItem(stack);
        if(data == -1) {
            return aimItem;
        }
        int nextData = data + 2;
        if(!skinnedStateItems.containsKey(nextData)) {
            skinnedStateItems.put(nextData, new ItemBuilder(getMaterial()).setName(getDisplayName()).setCustomModelData(nextData).build());
        }
        return skinnedStateItems.get(nextData);
    }


    public void buildStateItems() {
        if(reloadItem == null) {
            this.reloadItem = new ItemBuilder(getMaterial()).setName(getDisplayName()).setCustomModelData(getCustomModelData() + 1).build();
        }
        if(aimItem == null) {
            this.aimItem = new ItemBuilder(getMaterial()).setName(getDisplayName()).setCustomModelData(getCustomModelData() + 2).build();
        }
    }

    public void updateStateInventory(Player player, ItemStack stack) {
        GunPlayer gunPlayer = GunPlayer.of(player);
        ItemMeta meta = stack.getItemMeta();
        int skin = getSkinDataByItem(stack);
        switch (gunPlayer.getState()) {
            case AIMING:
                meta.setCustomModelData(skin == -1 ? getCustomModelData() + 2 : skin + 2);
                break;
            case RELOADING:
                meta.setCustomModelData(skin == -1 ? getCustomModelData() + 1 : skin + 1);
                break;
            case IDLE:
                meta.setCustomModelData(skin == -1 ? getCustomModelData() : skin);
                break;
        }
        stack.setItemMeta(meta);
        player.updateInventory();
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
        lore.add(" §8╔══ §aGeneral");
        lore.add(" §8╠ §7Type§8: §b" + type.getDisplayName());
        lore.add(" §8╠ §7Magazine§8: §b" + maxAmmo);
        if (!smallLore) {
            lore.add(" §8╠══ §eStats");
            lore.add(" §8╠ §7Range§8: §b" + range);
            lore.add(" §8╠ §7RPM§8: §b" + fireRate);
            lore.add(" §8╠ §7Spread§8: §b" + spread);
            lore.add(" §8╠ §7Sneak Spread§8: §b" + sneakSpread);
            lore.add(" §8╠ §7Reload time§8: §b" + reloadTimeInTicks / 20 + "s");
            lore.add(" §8╠══ §cDamage");
            lore.add(" §8╠ §7Head§8: §b" + headDamage);
            lore.add(" §8╠ §7Body§8: §b" + bodyDamage);
            lore.add(" §8╠ §7Legs§8: §b" + legDamage);
            lore.add(" §8╠══ §3Attachments §8(§3" + attachments.size() + "§8/§3" + getAttachmentSlots() + "§8)");
            for (int i = 0; i < attachmentSlots; i++) {
                if (attachments.size() >= i + 1) {
                    Attachment attachment = attachments.get(i);
                    lore.add(" §8╠ §b" + attachment.getDisplayName());
                } else {
                    lore.add(" §8╠ §7§oEmpty");
                }
            }
        }
        lore.add(" §8╚══");
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
                if (sound.getSound() == null) {
                    location.getWorld().playSound(location, sound.getSoundName(), sound.getVolume(), sound.getPitch());
                } else {
                    location.getWorld().playSound(location, sound.getSound(), sound.getVolume(), sound.getPitch());
                }
            } else {
                if (sound.getSound() == null) {
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


    public List<Attachment> getAttachments(Game game, ItemStack stack, boolean cached) {
        if (cached) {
            if (Gun.ATTACHMENT_CACHE.asMap().containsKey(stack)) {
                return ATTACHMENT_CACHE.getIfPresent(stack);
            }
        }
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
        if (cached) {
            ATTACHMENT_CACHE.put(stack, attachments);
        }
        return attachments;
    }

    private long addStatsValue(SeasonData seasonData, String key) {
        long current = seasonData.getStats().getOrDefault(key, 0l) + 1;
        seasonData.getStats().put(key, current);
        return current;
    }

    protected void rawShoot(Game game, Player player, GunPlayer gunPlayer, SeasonData seasonData, ItemStack stack) {
        double normalSpread = this.spread;
        double sneakSpread = this.sneakSpread;
        double range = this.range;

        for (Attachment attachment : getAttachments(game, stack, true)) {
            normalSpread = attachment.apply(AttachmentModifier.SPREAD, normalSpread);
            sneakSpread = attachment.apply(AttachmentModifier.SNEAK_SPREAD, sneakSpread);
            range = attachment.apply(AttachmentModifier.RANGE, range);
        }
        double spread = !player.isSneaking() ? normalSpread : sneakSpread;
        Location eyeLocation = player.getEyeLocation();
        Location particleOrigin = eyeLocation.clone();
        if(gunPlayer.getState() != GunPlayerState.AIMING) {
            Vector eyeOffset = VectorUtil.rotateVector(DEFAULT_EYE_ROTATION, eyeLocation.getYaw(), eyeLocation.getPitch());
            particleOrigin = particleOrigin.add(eyeOffset);
        } else {
            particleOrigin = particleOrigin.subtract(0, 0.2, 0);
        }
        particleOrigin = particleOrigin.add(eyeLocation.getDirection().normalize().multiply(2));

        Bullet bullet = new Bullet(particleOrigin, eyeLocation.clone(),
                VectorUtil.getRandomizedDirection(player, spread), range);


        List<RaytraceHit> hits = bullet.invoke();

        int damageReducePercentage = 0;
        for (RaytraceHit hit : hits) {
            if (hit instanceof RaytraceEntityHit entityHit) {
                if (!entityHit.getTarget().getName().equals(player.getName())) {
                    if (entityHit.getTarget() instanceof Player targetPlayer) {
                        GunPlayer target = GunPlayer.of(targetPlayer);
                        if (target.isShielded()) {
                            double angle = getAngleBetweenPoints(targetPlayer.getLocation(), player.getLocation());
                            if (angle <= 1.1) {
                                targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1f, 1);
                                spawnParticle(game, gunPlayer, particleOrigin, entityHit.getPoint());
                                return;
                            }
                        }
                    }

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
                    if (damageReducePercentage > 0) {
                        damage -= (damage / 100) * damageReducePercentage;
                    }

                    if (damage > 0) {
                        LivingEntity entity = (LivingEntity) entityHit.getTarget();
                        if (entity instanceof Player target) {
                            GunPlayer targetGunPlayer = GunPlayer.of(target);
                            targetGunPlayer.setLastDamageReceived(damageType);
                        }
                        entity.setNoDamageTicks(0);
                        entity.damage(damage, player);
                        entity.setVelocity(new Vector());
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
                if (!wallbangTypes.containsKey(blockHit.getTarget().getType())) {
                    blockHit.getTarget().getWorld().spawnParticle(Particle.BLOCK_DUST, blockHit.getPoint(), 2,
                            blockHit.getTarget().getBlockData());
                    blockHit.getPoint().getWorld().playSound(blockHit.getPoint(), "impact.cement", 0.35f, 1);
                    spawnParticle(game, gunPlayer, particleOrigin, blockHit.getPoint());
                    return;
                }
                if (blockHit.getTarget().getType().name().contains("GLASS")) {
                    blockHit.getPoint().getWorld().playSound(blockHit.getPoint(), "impact.glass", 0.35f, 1);
                } else if (blockHit.getTarget().getType().name().contains("LOG") || blockHit.getTarget().getType().name().contains("PLANKS")) {
                    blockHit.getPoint().getWorld().playSound(blockHit.getPoint(), "impact.tree", 0.35f, 1);
                }
                damageReducePercentage += wallbangTypes.get(blockHit.getTarget().getType());
            }
        }
        spawnParticle(game, gunPlayer, particleOrigin, bullet.getEndPoint());
    }


    public void shoot(Game game, Player player, ItemStack stack) {
        GunPlayer gunPlayer = GunPlayer.of(player);

        GunShootEvent shootEvent = new GunShootEvent(gunPlayer, this, false);
        game.getItemRegistry().getEventBus().publish(shootEvent);

        if (shootEvent.isCancelled()) {
            return;
        }
        SeasonData seasonData = gunPlayer.getUser().getSeasonData();
        addStatsValue(seasonData, "shots");

        rawShoot(game, player, gunPlayer, seasonData, stack);


    }

    private double getAngleBetweenPoints(Location source, Location target) {
        Vector inBetween = target.clone().subtract(source).toVector();
        Vector forward = source.getDirection();
        return forward.angle(inBetween);
    }

    private void spawnParticle(Game game, GunPlayer gunPlayer, Location particleOrigin, Location location) {
        //TODO: muzzle flash particleOrigin.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleOrigin, 0);
        BulletTracer tracer = game.getTracerRegistry().getByUser(gunPlayer.getUser());
        if (tracer == null) {
            ParticleUtil.spawnParticleAlongLine(particleOrigin, location, shootParticle, 20, 0);
        } else {
            tracer.play(particleOrigin, location);
        }
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


    public void reload(Player player, Game game, ItemStack stack) {
        GunPlayer gunPlayer = GunPlayer.of(player);
        if (gunPlayer.isReloading()) {
            return;
        }
        List<Attachment> attachments = getAttachments(game, stack, true);
        int maxAmmo = this.maxAmmo;
        for (Attachment attachment : attachments) {
            maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
        }

        int current = game.getItemAmmoProvider().getAmmo(player, stack);
        if (current >= maxAmmo) {
            return;
        }
        int needed = maxAmmo - current;

        int ammoToGive;

        int ammo = !unlimitedAmmo ? AmmoType.getAmmoInInventory(player, ammoType) : needed;
        if(player.getGameMode() == GameMode.CREATIVE) {
            ammo = needed;
        }
        if (ammo >= 1) {
            GunReloadEvent reloadEvent = new GunReloadEvent(player, gunPlayer, this, this.reloadTimeInTicks, false);
            game.getItemRegistry().getEventBus().publish(reloadEvent);
            if (reloadEvent.isCancelled()) {
                return;
            }
            int reloadTimeInTicks = reloadEvent.getReloadTimeInTicks();

            if (ammo < needed) {
                AmmoType.takeAmmo(player, ammoType, ammo);
                ammoToGive = current + ammo;
            } else {
                AmmoType.takeAmmo(player, ammoType, needed);
                ammoToGive = maxAmmo;
            }
            int finalAmmoToGive = ammoToGive;
            Bukkit.getScheduler().runTaskLater(game, () -> {
                if (!gunPlayer.isReloading()) {
                    return;
                }
                game.getItemAmmoProvider().setAmmo(player, stack, finalAmmoToGive);
                gunPlayer.setState(GunPlayerState.IDLE);
                player.updateInventory();
                playSound(player, GunSoundType.RELOAD_IN);
                updateStateInventory(player, stack);
            }, reloadTimeInTicks);
            gunPlayer.setState(GunPlayerState.RELOADING);
            updateStateInventory(player, stack);
            gunPlayer.setReloadingGunId(getId());
            gunPlayer.setReloadingEndTimestamp(System.currentTimeMillis() + 50 * reloadTimeInTicks);
            playSound(player, GunSoundType.RELOAD_OUT);
        }


    }
    @CustomItemEvent
    public void onChangeSlot(Player player, Game game, CustomItemHolder holder, PlayerItemHeldEvent event) {
        final ItemStack stack = player.getInventory().getItem(event.getNewSlot());
        if(stack != null) {
            if(stack.hasItemMeta()) {
                if(stack.getItemMeta().hasDisplayName()) {
                    if(stack.getItemMeta().getDisplayName().equals(getDisplayName())) {
                        playSound(player, GunSoundType.EQUIP);
                        updateStateInventory(player, stack);
                    }
                }
            }
        }
    }

    @CustomItemEvent
    public void onUpdate(Player player, Game game, CustomItemHolder holder, CustomItemHolder event) {
        if (event.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }

        GunPlayer gunPlayer = GunPlayer.of(player);
        if (gunPlayer.isShielded() && type != GunType.PISTOL) {
            return;
        }

        if (zoom > 0) {
            if (player.isSneaking()) {
                if(!gunPlayer.isReloading()) {
                    if (!gunPlayer.isAiming()) {
                        gunPlayer.setState(GunPlayerState.AIMING);
                        playSound(player, GunSoundType.AIM_IN);
                        if(zoomItem == null) {
                            updateStateInventory(player, holder.getStack());
                        }
                    }
                    if(zoomItem != null) {
                        ItemUtil.sendFakeMainHeadEquipment(player, zoomItem);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, zoom - 1, false, false));
                }
            } else {
                if (gunPlayer.isAiming()) {
                    playSound(player, GunSoundType.AIM_OUT);
                    gunPlayer.setState(GunPlayerState.IDLE);
                    player.removePotionEffect(PotionEffectType.SLOW);
                    updateStateInventory(player, holder.getStack());
                }
            }
        }
        long currentUpdateTick = gunPlayer.getCurrentUpdateTick();
        if (currentUpdateTick >= Integer.MAX_VALUE) {
            currentUpdateTick = 0;
        }
        int ammo = game.getItemAmmoProvider().getAmmo(player, holder.getStack());
        List<Attachment> attachments = getAttachments(game, holder.getStack(), true);
        int fireRate = this.fireRate;
        int maxAmmo = this.maxAmmo;
        for (Attachment attachment : attachments) {
            fireRate = attachment.apply(AttachmentModifier.FIRE_RATE, fireRate);
            maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
        }
        if (gunPlayer.isReloading()) {
            if (!gunPlayer.getReloadingGunId().equals(getId())) {
                gunPlayer.setState(GunPlayerState.IDLE);
                player.updateInventory();
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
                if(gunPlayer.getState() == GunPlayerState.AIMING) {
                    if(gunPlayer.getRecoilTask() == null) {
                        RecoilTask task = new RecoilTask(this, player, 15);
                        new Timer().schedule(task, 15, 15);
                        gunPlayer.setRecoilTask(task);
                    }
                    gunPlayer.getRecoilTask().setTickCounter(0);
                }
                playSound(player, GunSoundType.SHOOT);
                ammo--;
                game.getItemAmmoProvider().setAmmo(player, holder.getStack(), ammo);
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
            if (disableInteraction) {
                event.setCancelled(true);
                event.setUseItemInHand(Event.Result.DENY);
                event.setUseInteractedBlock(Event.Result.DENY);
            }
            if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                reload(event.getPlayer(), game, holder.getStack());
            } else if (allowedInteract) {
                GunPreShootEvent preShootEvent = new GunPreShootEvent(player, GunPlayer.of(player), this, false);
                game.getItemRegistry().getEventBus().publish(preShootEvent);
                if(preShootEvent.isCancelled()) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not allowed to use this gun here.");
                    return;
                }
                int ammo = game.getItemAmmoProvider().getAmmo(player, holder.getStack());
                if (ammo <= 0) {
                    playSound(player, GunSoundType.LOW_AMMO);
                    return;
                }
                GunPlayer gunPlayer = GunPlayer.of(player);
                gunPlayer.setLastInteract(System.currentTimeMillis());
            }

        }
    }

    @Override
    public ItemStack build() {
        ItemStack stack = super.build();
        ItemMeta meta = stack.getItemMeta();
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("generic_attac_speed", 10, AttributeModifier.Operation.ADD_NUMBER));
        stack.setItemMeta(meta);
        return stack;
    }








}
