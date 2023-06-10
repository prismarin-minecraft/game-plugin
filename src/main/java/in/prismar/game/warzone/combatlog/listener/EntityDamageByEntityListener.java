package in.prismar.game.warzone.combatlog.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.game.warzone.combatlog.CombatLogService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageByEntityListener implements Listener {

    @Inject
    private CombatLogService service;

    @Inject
    private WarzoneService warzoneService;

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player && event.getDamager() instanceof Player damager) {
            if(!service.getRegionProvider().isInRegionWithFlag(player.getLocation(), "pvp") && warzoneService.isInWarzone(player)) {
                if(!service.addCombatLog(player, damager)) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cYou are now in combat");
                }
                if(!service.addCombatLog(damager, player)) {
                    damager.sendMessage(PrismarinConstants.PREFIX + "§cYou are now in combat");
                }
            }

        }
    }
}
