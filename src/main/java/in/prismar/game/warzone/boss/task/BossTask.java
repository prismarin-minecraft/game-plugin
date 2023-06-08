package in.prismar.game.warzone.boss.task;

import in.prismar.game.warzone.boss.Boss;
import in.prismar.game.warzone.boss.BossService;
import in.prismar.library.common.text.Progress;
import io.lumine.mythic.api.adapters.AbstractBossBar;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class BossTask implements Runnable {

    private static final double BOSS_BAR_DISTANCE = 60 * 60;
    private static final Progress PROGRESS = new Progress("§8<§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§7▬§8>", 45, "§7▬", "§c▬", false);

    private final BossService service;

    @Override
    public void run() {
        for (Boss boss : service.getBosses()) {
            if(boss.getMythicMob() == null) {
                boss.setMythicMob(MythicBukkit.inst().getMobManager().getMythicMob(boss.getId()).orElse(null));
            }
            if(boss.getMythicMob() == null) {
                continue;
            }
            for (ActiveMob mob : MythicBukkit.inst().getMobManager().getActiveMobs()) {
                if(!mob.getName().equals(boss.getMythicMob().getDisplayName().get())) {
                    continue;
                }
                if(mob.isDead()) {
                    continue;
                }
                BossBar bossBar = boss.getBossBars().containsKey(mob.getUniqueId()) ?
                        boss.getBossBars().get(mob.getUniqueId()) : service.createBossbar(boss, mob);

                Location location = mob.getLocation().toPosition().toLocation();
                for (Player player : location.getWorld().getPlayers()) {
                    if (player.getLocation().distanceSquared(location) <= BOSS_BAR_DISTANCE) {
                        bossBar.addPlayer(player);
                    }
                }
                Iterator<Player> iterator = bossBar.getPlayers().iterator();
                while (iterator.hasNext()) {
                    Player player = iterator.next();
                    if(!player.getWorld().getName().equals(location.getWorld().getName())) {
                        bossBar.removePlayer(player);
                        continue;
                    }
                    if (player.getLocation().distanceSquared(location) > BOSS_BAR_DISTANCE) {
                        bossBar.removePlayer(player);
                    }
                }
                long health = (long)mob.getEntity().getHealth();
                long maxHealth = (long)boss.getMythicMob().getHealth().get();
                bossBar.setTitle(mob.getDisplayName() + " " + PROGRESS.show(health, maxHealth));
            }
        }
    }
}
