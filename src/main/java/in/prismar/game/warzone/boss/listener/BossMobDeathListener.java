package in.prismar.game.warzone.boss.listener;

import in.prismar.game.warzone.boss.Boss;
import in.prismar.game.warzone.boss.BossService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class BossMobDeathListener implements Listener {

    @Inject
    private BossService service;

    @EventHandler
    public void onCall(MythicMobDeathEvent event) {
        Boss boss = service.getBossById(event.getMobType().getInternalName());
        if(boss != null) {
            service.deleteBossbar(boss, event.getMob().getUniqueId());
        }
    }
}
