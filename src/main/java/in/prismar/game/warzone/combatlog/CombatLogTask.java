package in.prismar.game.warzone.combatlog;

import in.prismar.api.PrismarinConstants;
import lombok.AllArgsConstructor;
import org.bukkit.Sound;

import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class CombatLogTask implements Runnable {

    private CombatLogService service;

    @Override
    public void run() {
        for(Map.Entry<UUID, CombatLog> entry : service.getCombatLogs().entrySet()) {
            CombatLog log = entry.getValue();
            if(System.currentTimeMillis() >= log.getUntil() || service.getRegionProvider().isInRegionWithFlag(log.getPlayer().getLocation(), "pvp")) {
                if(log.getPlayer().isOnline()) {
                    log.getPlayer().sendMessage(PrismarinConstants.PREFIX + "§cYou are not in combat any more.");
                }
                service.removeCombatLog(log.getPlayer());
            }

        }
    }
}
