package in.prismar.game.item.impl.medical;

import in.prismar.game.Game;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class MedicalSyringeItem extends CustomItem {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");
    private static final Set<UUID> CURRENTLY_HEALING = new HashSet<>();

    private final int healingTicks = 40;
    private final double healing = 6;

    public MedicalSyringeItem() {
        super("MedicalSyringe", Material.FEATHER, "§fMedical Syringe");
        setCustomModelData(1);
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        if(CURRENTLY_HEALING.contains(player.getUniqueId())) {
            return;
        }
        event.setCancelled(true);
        player.playSound(player.getLocation(), Sound.BLOCK_HONEY_BLOCK_HIT, 0.7f, 2);
        CURRENTLY_HEALING.add(player.getUniqueId());
        long end = System.currentTimeMillis() + (50 * healingTicks);
        Scheduler.runTimerFor(1, 1, healingTicks, new SchedulerRunnable() {
            @Override
            public void run() {
                if(player.isOnline()) {
                    if(ItemUtil.hasItemInHandAndHasDisplayName(player, true)) {
                        if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(getDisplayName())) {
                            long difference = end - System.currentTimeMillis();
                            double result = (double) difference / 1000.0;
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                    new TextComponent("§cHealing " + DECIMAL_FORMAT.format(result)));
                            if(getCurrentTicks() <= 1) {
                                CURRENTLY_HEALING.remove(player.getUniqueId());
                                ItemUtil.takeItemFromHand(player, true);
                                double next = player.getHealth() + healing;
                                player.setHealth(MathUtil.clamp(next, 1, 20));
                                player.playSound(player.getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK, 0.7f, 2);
                                return;
                            }
                            return;
                        }
                    }
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent("§cHealing aborted"));
                if(CURRENTLY_HEALING.contains(player.getUniqueId())) {
                    CURRENTLY_HEALING.remove(player.getUniqueId());
                }
                cancel();
            }
        });
    }

}
