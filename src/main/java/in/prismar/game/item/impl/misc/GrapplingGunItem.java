package in.prismar.game.item.impl.misc;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.raytrace.Raytrace;
import in.prismar.library.spigot.raytrace.hitbox.RaytraceHitboxHelper;
import in.prismar.library.spigot.raytrace.result.RaytraceBlockHit;
import in.prismar.library.spigot.raytrace.result.RaytraceResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftSheep;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GrapplingGunItem extends CustomItem {

    private static final Map<UUID, GrapplingEntry> entries = new HashMap<>();
    private static final Map<UUID, Long> noFallDamage = new HashMap<>();

    private static final int RANGE = 40;
    private static final int MAX_RANGE = 50 * 50;
    private static final int RANGE_UNHOOK = 5;
    private static final double BOOST = 1.6;
    private static final long NO_DAMAGE_TIMER = 5 * 1000;
    private static final long COOLDOWN = 3 * 1000;
    private static final long SOUND_DELAY = 900;

    private static final float SPEED = 0.8f;


    private final UserProvider<User> userProvider;

    private final int customModelDataWithoutHook = 6;

    public GrapplingGunItem() {
        super("GrapplingGun", Material.BOWL, "§6Grappling Gun");
        allFlags();
        setCustomModelData(5);

        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
    }

    private void updateItem(Player player, CustomItemHolder holder, boolean hook) {
        ItemStack stack = holder.getStack();
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(hook ? getCustomModelData() : customModelDataWithoutHook);
        stack.setItemMeta(meta);
        player.updateInventory();
    }

    @CustomItemEvent
    public void onDamage(Player player, Game game, CustomItemHolder holder, EntityDamageEvent event) {
        if (noFallDamage.containsKey(player.getUniqueId()) && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            long time = noFallDamage.get(player.getUniqueId());
            if (System.currentTimeMillis() < time) {
                event.setCancelled(true);
            }
        }
    }

    @CustomItemEvent
    public void onUpdate(Player player, Game game, CustomItemHolder holder, CustomItemHolder event) {
        if (noFallDamage.containsKey(player.getUniqueId())) {
            long time = noFallDamage.get(player.getUniqueId());
            if (System.currentTimeMillis() >= time) {
                noFallDamage.remove(player.getUniqueId());
            }
        }
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (entries.containsKey(player.getUniqueId())) {
                GrapplingEntry entry = entries.get(player.getUniqueId());
                long diff = System.currentTimeMillis() - entry.getStarted();
                if (diff >= 500) {
                    unhook(player, holder);
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.8f, 1f);
                    noFallDamage.put(player.getUniqueId(), System.currentTimeMillis() + (NO_DAMAGE_TIMER/2));
                }
            }
            return;
        }
        if (entries.containsKey(player.getUniqueId())) {
            return;
        }

        User user = userProvider.getUserByUUID(player.getUniqueId());
        if (!user.isLocalTimestampAvailable("grappling.gun")) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cPlease wait a moment before you use it again");
            return;
        }

        Raytrace raytrace = new Raytrace(player.getEyeLocation(), player.getEyeLocation().getDirection(),
                RaytraceHitboxHelper.collectPossibleBlockHitboxes(player.getEyeLocation(), player.getEyeLocation().getDirection(), RANGE));
        RaytraceResult result = raytrace.ray(RANGE);
        if (!result.getHits().isEmpty()) {
            if (result.getHits().stream().findFirst().get() instanceof RaytraceBlockHit blockHit) {
                player.playSound(player.getLocation(), "grappling", 0.7f, 1f);
                Sheep target = player.getWorld().spawn(blockHit.getPoint(), Sheep.class);
                target.setAI(false);
                target.setBaby();
                target.setSilent(true);
                target.setInvisible(true);
                target.setInvulnerable(true);
                target.setGravity(false);
                CraftSheep craftSheep = (CraftSheep) target;
                CraftPlayer craftPlayer = (CraftPlayer) player;
                ClientboundSetEntityLinkPacket packet = new ClientboundSetEntityLinkPacket(craftSheep.getHandle(), craftPlayer.getHandle());
                sendPacket(player, packet);
                user.setLocalTimestamp("grappling.gun", System.currentTimeMillis() + COOLDOWN);
                updateItem(player, holder, false);
                entries.put(player.getUniqueId(), new GrapplingEntry(player, player.getLocation().clone(), target.getLocation(), target, 0, System.currentTimeMillis(), Bukkit.getScheduler().runTaskTimer(game, () -> {
                    if (entries.containsKey(player.getUniqueId())) {
                        if (!player.isOnline()) {
                            unhook(player, holder);
                            return;
                        }
                        if (player.isDead()) {
                            unhook(player, holder);
                            return;
                        }
                        GrapplingEntry entry = entries.get(player.getUniqueId());
                        Location location = player.getLocation();
                        Location end = entry.getEnd();
                        if (!location.getWorld().getName().equals(end.getWorld().getName())) {
                            unhook(player, holder);
                            return;
                        }
                        Location start = entry.getStart();
                        if (!start.getWorld().getName().equals(location.getWorld().getName())) {
                            unhook(player, holder);
                            return;
                        }
                        double distance = location.distanceSquared(end);
                        if (distance > MAX_RANGE) {
                            unhook(player, holder);
                            return;
                        }
                        if (distance < RANGE_UNHOOK) {
                            unhook(player, holder);
                            noFallDamage.put(player.getUniqueId(), System.currentTimeMillis() + NO_DAMAGE_TIMER);
                            //Boost
                            Vector vector = end.toVector().subtract(location.toVector())
                                    .normalize().multiply(BOOST);
                            player.setVelocity(vector);
                            return;
                        }
                        if (player.isSneaking()) {
                            Vector vector = end.toVector().subtract(location.toVector())
                                    .normalize().multiply(SPEED);
                            player.setVelocity(vector);

                            long difference = System.currentTimeMillis() - entry.getLastSoundPlayed();
                            if (difference >= SOUND_DELAY) {
                                entry.setLastSoundPlayed(System.currentTimeMillis());
                                player.playSound(player.getLocation(),"grappling.pull", 0.5f, 1f);
                            }
                        }
                    }
                }, 1, 1)));

            }
        }
    }

    private void unhook(Player player, CustomItemHolder holder) {
        remove(player.getUniqueId());
        updateItem(player, holder, true);
        player.stopSound("grappling.pull");
    }

    protected void sendPacket(Player player, Packet<?> packet) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().connection.send(packet);
    }

    private void remove(UUID uuid) {
        GrapplingEntry entry = entries.remove(uuid);
        for (Entity entity : entry.getEnd().getWorld().getNearbyEntities(entry.getEnd(), 2, 2, 2, e -> e instanceof Sheep)) {
            entity.remove();
        }
        entry.getTask().cancel();
    }

    @AllArgsConstructor
    @Getter
    public class GrapplingEntry {

        private final Player player;
        private final Location start;
        private final Location end;
        private final Sheep target;
        @Setter
        private long lastSoundPlayed;
        private long started;

        @Setter
        private BukkitTask task;

    }
}