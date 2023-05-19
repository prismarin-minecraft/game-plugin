package in.prismar.game.item.impl.misc;

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
import in.prismar.game.party.Party;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class UAVItem extends CustomItem {

    public static Set<UUID> UAV_ACTIVE = new HashSet<>();
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
        if(UAV_ACTIVE.contains(player.getUniqueId())) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou have currently an UAV running.");
            return;
        }
        ItemUtil.takeItemFromHand(player, true);
        List<Player> players = new ArrayList<>();
        Party party = game.getPartyRegistry().getPartyByPlayer(player);
        for(Player target : player.getWorld().getPlayers()) {
            if(target.getLocation().distance(player.getLocation()) <= RANGE && !player.getUniqueId().equals(target.getUniqueId())) {
                Party other = game.getPartyRegistry().getPartyByPlayer(target);
                if(party != null && other != null) {
                    if(party.getOwner().getUniqueId().equals(other.getOwner().getUniqueId())) {
                        continue;
                    }
                }
                try {
                    game.getGlowingEntities().setGlowing(target, player, ChatColor.GREEN);
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
                players.add(target);
            }
        }
        UAV_ACTIVE.add(player.getUniqueId());
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
                    UAV_ACTIVE.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                ticks+= 20;
            }
        }.runTaskTimer(game, 20, 20);

    }

}
