package in.prismar.game.warzone;

import in.prismar.api.PrismarinApi;
import in.prismar.api.clan.ClanStatsProvider;
import in.prismar.api.warp.WarpProvider;
import in.prismar.api.warzone.WarzoneProvider;
import in.prismar.game.Game;
import in.prismar.game.warzone.config.WarzoneConfig;
import in.prismar.game.warzone.task.AirdropTask;
import in.prismar.game.warzone.task.WarzoneAmbienceTask;
import in.prismar.game.warzone.tombstone.Tombstone;
import in.prismar.game.warzone.tombstone.TombstoneTask;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.location.LocationUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class WarzoneService implements WarzoneProvider {

    private static final String WARP_NAME = "warzone";

    private final WarpProvider warpProvider;

    private final Game game;

    private final WarzoneConfig config;

    private List<Tombstone> tombstones;

    private AirdropTask airdropTask;

    private ClanStatsProvider clanStatsProvider;

    public WarzoneService(Game game) {
        this.game = game;
        this.warpProvider = PrismarinApi.getProvider(WarpProvider.class);
        this.tombstones = new CopyOnWriteArrayList<>();
        this.config = new WarzoneConfig(game.getDefaultDirectory());

        Bukkit.getScheduler().runTaskTimer(game, new WarzoneAmbienceTask(this), 20, 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new TombstoneTask(this), 20, 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, airdropTask = new AirdropTask(this), 20 * 5, 20 * 5);
    }

    public Tombstone createTombstone(Player player, List<ItemStack> stacks) {
        List<ItemStack> cleared = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack != null) {
                if (stack.getType() != Material.AIR) {
                    cleared.add(stack);
                }
            }
        }
        final int checkDown = 20;

        Location start = player.getLocation().clone().add(0, 2, 0).getBlock().getLocation();
        Location location = start.clone();
        for (int i = 0; i < checkDown; i++) {
            location = start.clone().subtract(0, i, 0);
            if (location.getBlock().getType().isSolid()) {
                break;
            }
        }

        Tombstone tombstone = new Tombstone();
        tombstone.setDespawnTimestamp(System.currentTimeMillis() + (1000 * 60 * 2));
        tombstone.setInventory(Bukkit.createInventory(null, 9 * 4, player.getName()));
        tombstone.getInventory().setContents(cleared.toArray(new ItemStack[0]));
        Hologram hologram = new Hologram(LocationUtil.getCenterOfBlock(location.getBlock().getLocation().clone().add(0, 1, 0)));
        hologram.addLine(HologramLineType.TEXT, "§c" + player.getName(), false);
        hologram.addLine(HologramLineType.TEXT, "§8[§c" + getFormattedTombstoneTime(tombstone) + "§8]");
        hologram.addLine(HologramLineType.ITEM_HEAD, new ItemBuilder(Material.LEAD).setCustomModelData(4).build());
        hologram.interaction(player1 -> {
            Bukkit.getScheduler().runTask(game, () -> {
                player1.openInventory(tombstone.getInventory());
                player1.playSound(player1.getLocation(), Sound.BLOCK_BARREL_OPEN, 0.6f, 1);
            });
        });
        hologram.enable();
        tombstone.setHologram(hologram);

        this.tombstones.add(tombstone);
        return tombstone;
    }

    public boolean isTombstoneEmpty(Tombstone tombstone) {
        for (ItemStack stack : tombstone.getInventory().getContents()) {
            if (stack != null) {
                if (stack.getType() != Material.AIR) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getFormattedTombstoneTime(Tombstone tombstone) {
        long seconds = (tombstone.getDespawnTimestamp() - System.currentTimeMillis()) / 1000;
        return toFormattedTime(seconds);
    }

    public String toFormattedTime(long seconds) {
        return String.format("%02d:%02d:%02d",
                (seconds / 60 / 60) % 24,
                (seconds / 60) % 60,
                seconds % 60);
    }

    public void teleportToWarzone(Player player) {
        warpProvider.teleport(player, WARP_NAME);
    }

    public Location getWarzoneLocation() {
        return warpProvider.getWarp(WARP_NAME);
    }

    @Override
    public boolean isInWarzone(Player player) {
        Location location = warpProvider.getWarp(WARP_NAME);
        if (location != null) {
            return player.getWorld().getName().equals(location.getWorld().getName());
        }
        return false;
    }

    public ClanStatsProvider getClanStatsProvider() {
        if (clanStatsProvider == null) {
            clanStatsProvider = PrismarinApi.getProvider(ClanStatsProvider.class);
        }
        return clanStatsProvider;
    }
}
