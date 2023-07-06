package in.prismar.game.warzone.dungeon.listener;

import in.prismar.game.warzone.boss.Boss;
import in.prismar.game.warzone.boss.BossService;
import in.prismar.game.warzone.dungeon.Dungeon;
import in.prismar.game.warzone.dungeon.DungeonService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import io.lumine.mythic.core.spawning.spawners.MythicSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class DungeonBossMobDeathListener implements Listener {

    @Inject
    private BossService service;

    @Inject
    private DungeonService dungeonService;

    @EventHandler
    public void onCall(MythicMobDeathEvent event) {
        Boss boss = service.getBossById(event.getMobType().getInternalName());
        if(boss != null) {
            Dungeon dungeon = dungeonService.getDungeonByEndBossId(event.getMobType().getInternalName());
            if(dungeon != null) {
                dungeon.reduceTimeTo(dungeon.getReduceTimer());
            }
        }
    }
}
