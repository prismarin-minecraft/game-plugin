package in.prismar.game.warzone.boss;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.clan.Clan;
import in.prismar.api.clan.ClanBuff;
import in.prismar.api.clan.ClanProvider;
import in.prismar.api.configuration.node.ConfigNode;
import in.prismar.api.configuration.node.ConfigNodeProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.warzone.boss.task.BossTask;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Service;
import io.lumine.mythic.core.mobs.ActiveMob;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.*;

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
    private final List<Boss> bosses;

    public BossService(Game game) {
        this.game = game;
        this.bosses = new ArrayList<>();

        register("toro_type02", ClanBuff.GENERATOR_MULTIPLIER);
        register("zaku", ClanBuff.GENERATOR_MULTIPLIER);
        register("kindletron_2", ClanBuff.GENERATOR_MULTIPLIER);
        Bukkit.getScheduler().runTaskTimer(game, new BossTask(this), 20, 20);
    }

    public Boss register(String id, ClanBuff buff) {
        Boss boss = new Boss();
        boss.setId(id);
        boss.setBuff(buff);
        boss.setBossBars(new HashMap<>());
        boss.setDamagers(new HashMap<>());
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

    public void addDamage(Boss boss, ActiveMob mob, Player player, double damage) {
        if(!boss.getDamagers().containsKey(mob.getUniqueId())) {
            boss.getDamagers().put(mob.getUniqueId(), new HashMap<>());
        }
        Map<UUID, BossDamager> damagers = boss.getDamagers().get(mob.getUniqueId());
        if(!damagers.containsKey(player.getUniqueId())) {
            BossDamager damager = new BossDamager(player.getUniqueId(), player.getName());
            damager.setDamage(damage);
            damagers.put(player.getUniqueId(), damager);
        } else {
            BossDamager damager = damagers.get(player.getUniqueId());
            damager.setDamage(damager.getDamage() + damage);
        }
    }

    public void handleBossDeath(Boss boss, ActiveMob mob) {
        if(boss.getDamagers().containsKey(mob.getUniqueId())) {
            Map<UUID, BossDamager> damagers = boss.getDamagers().get(mob.getUniqueId());
            List<BossDamager> sorted = new ArrayList<>(damagers.values()).stream().sorted((o1, o2) -> Double.compare(o2.getDamage(), o1.getDamage())).limit(10).toList();
            double balance = game.getConfigNodeFile().getDouble("Boss." + boss.getMythicMob().getInternalName() + ".Start balance", 60000);
            double reducePerPlacement = game.getConfigNodeFile().getDouble("Boss." + boss.getMythicMob().getInternalName() + ".Reduce per placement", 65000);

            final String arrow = PrismarinConstants.ARROW_RIGHT + " ";
            final String dot = "    " + PrismarinConstants.DOT + " ";
            for(Player online : Bukkit.getOnlinePlayers()) {
                if(game.getWarzoneService().isInWarzone(online) || damagers.containsKey(online.getUniqueId())) {
                    online.sendMessage(" ");
                    online.sendMessage(PrismarinConstants.BORDER);
                    online.sendMessage(" ");
                    online.sendMessage(arrow + "§b" + mob.getDisplayName() + " §7has been killed");
                    online.sendMessage(" ");
                    online.sendMessage(arrow + "§bRewards§8:");
                    for (int i = 0; i < sorted.size(); i++) {
                        double receiveMoney = balance - (reducePerPlacement * i);
                        BossDamager damager = sorted.get(i);
                        final String place = i == 0 ? "§e§l1st" : i == 1 ? "§7§l2nd" : i == 2 ? "§6§l3rd" : "§2" + (i + 1);
                        online.sendMessage(dot + place + " " + damager.getName() + " §8| §c"+Math.round(damager.getDamage())+" damage §8| §a+" + NumberFormatter.formatDoubleToThousands(receiveMoney) + "$ ");
                    }
                    online.sendMessage(" ");
                    online.sendMessage(PrismarinConstants.BORDER);
                    online.sendMessage(" ");
                    online.playSound(online.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 0.3f, 1);
                }
            }

            UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);
            ClanProvider<Clan> clanProvider = PrismarinApi.getProvider(ClanProvider.class);
            for (int i = 0; i < sorted.size(); i++) {
                BossDamager damager = sorted.get(i);
                double receiveMoney = balance - (reducePerPlacement * i);

                User user = userProvider.getUserByUUID(damager.getUuid());
                user.getSeasonData().setBalance(user.getSeasonData().getBalance() + receiveMoney);
                userProvider.saveAsync(user, true);
                if(clanProvider.isInClan(damager.getUuid())) {
                    Clan clan = clanProvider.getClanByPlayer(damager.getUuid());
                    if(i == 0) {
                        clanProvider.giveBuff(clan, boss.getBuff());
                        clanProvider.sendPrefixMessage(clan, "§7Your clan received the buff §e" + boss.getBuff().getDisplayName() + " §7provided by §6" + damager.getName() + " §7for killing the boss §3" + mob.getDisplayName());
                    }
                    game.getWarzoneService().getClanStatsProvider().addBossFights(damager.getUuid());

                }
            }
            boss.getDamagers().remove(mob.getUniqueId());
        }
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
