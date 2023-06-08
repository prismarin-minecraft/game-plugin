package in.prismar.game.warzone.boss;

import in.prismar.game.Game;
import in.prismar.game.warzone.boss.task.BossTask;
import in.prismar.library.meta.anno.Service;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class BossService {

    private final Game game;
    private List<Boss> bosses;

    public BossService(Game game) {
        this.game = game;
        this.bosses = new ArrayList<>();

        register("toro_type02");
        register("zaku");
        Bukkit.getScheduler().runTaskTimer(game, new BossTask(this), 20, 20);
    }

    public Boss register(String id) {
        Boss boss = new Boss();
        boss.setId(id);
        boss.setBossBars(new HashMap<>());
        this.bosses.add(boss);
        return boss;
    }

    public BossBar createBossbar(Boss boss, ActiveMob mob) {
        BossBar bossBar = Bukkit.createBossBar(boss.getId(), BarColor.RED, BarStyle.SOLID);
        boss.getBossBars().put(mob.getUniqueId(), bossBar);
        return bossBar;
    }

    public void deleteBossbar(Boss boss, UUID uuid) {
        BossBar bossBar = boss.getBossBars().get(uuid);
        bossBar.removeAll();
        boss.getBossBars().remove(uuid);
    }



    public boolean existsBossById(String id) {
        return getBossById(id) != null;
    }

    public Boss getBossById(String id) {
        for(Boss boss : bosses) {
            if(boss.getId().equalsIgnoreCase(id)) {
                return boss;
            }
        }
        return null;
    }

}
