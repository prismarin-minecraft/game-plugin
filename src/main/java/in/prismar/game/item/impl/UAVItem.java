package in.prismar.game.item.impl;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.PrismarinProvider;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.game.Game;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class UAVItem extends CustomItem {

    private static final double RANGE = 350;
    private static final long TIME_IN_TICKS = 20 * 25;

    public UAVItem() {
        super("UAV", Material.STICK, "§aUAV");
        setCustomModelData(4);
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setCancelled(true);
        if(game.getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp")) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not allowed to use this item inside a safe region.");
            return;
        }
        ItemUtil.takeItemFromHand(player, true);
        List<Player> players = new ArrayList<>();
        for(Player target : player.getWorld().getPlayers()) {
            if(target.getLocation().distance(player.getLocation()) <= RANGE && !player.getUniqueId().equals(target.getUniqueId())) {
                try {
                    game.getGlowingEntities().setGlowing(target, player, ChatColor.GREEN);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
                players.add(target);
            }
        }
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_POWER_SELECT, 0.5f, 2);
        new BukkitRunnable() {
            long ticks = 0;
            @Override
            public void run() {
                if(!game.isCurrentlyInGame(player) || ticks >= TIME_IN_TICKS) {
                    if(player.isOnline()) {
                        for(Player target : players) {
                            if(target.isOnline()) {
                                try {
                                    game.getGlowingEntities().unsetGlowing(target, player);
                                } catch (ReflectiveOperationException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 0.5f, 2);
                        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
                        provider.recreateTablist(player);
                    }
                    cancel();
                    return;
                }
                ticks+= 20;
            }
        }.runTaskTimer(game, 20, 20);

    }

}
