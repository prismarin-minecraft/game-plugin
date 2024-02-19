package in.prismar.game.warzone.boss;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.clan.Clan;
import in.prismar.api.clan.ClanBuff;
import in.prismar.api.clan.ClanProvider;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.missions.MissionWrapper;
import in.prismar.game.warzone.boss.task.BossTask;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    private final List<Boss> bosses;
    private final ConfigStore configStore;

    @Inject
    private MissionWrapper missionWrapper;

    public BossService(Game game) {
        this.game = game;
        this.bosses = new ArrayList<>();
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);

        if(!game.getServer().getPluginManager().isPluginEnabled("MythicMobs")) {
           return;
        }
        register("toro_type02", ClanBuff.GENERATOR_MULTIPLIER);
        register("zaku", ClanBuff.GENERATOR_MULTIPLIER);
        register("kindletron_2", ClanBuff.GENERATOR_MULTIPLIER);
        register("zahar", ClanBuff.GENERATOR_MULTIPLIER, 2, "zahar", "zahar_abomination");
        Bukkit.getScheduler().runTaskTimer(game, new BossTask(this), 20, 20);

    }

    public Boss register(String id, ClanBuff buff, String... mobs) {
        Boss boss = MythicMobsWrapper.createBoss(id, buff, mobs);
        this.bosses.add(boss);
        return boss;
    }

    public Boss register(String id, ClanBuff buff, int lives, String... mobs) {
        Boss boss = register(id, buff, mobs);
        boss.setLives(lives);
        boss.setMaxLives(lives);
        return boss;
    }

    public BossBar createBossbar(Boss boss, UUID uuid) {
        BossBar bossBar = Bukkit.createBossBar(boss.getId(), BarColor.RED, BarStyle.SOLID);
        boss.getBossBars().put(uuid, bossBar);
        return bossBar;
    }

    public void deleteBossbar(Boss boss, UUID uuid) {
        BossBar bossBar = boss.getBossBars().get(uuid);
        bossBar.removeAll();
        boss.getBossBars().remove(uuid);
    }

    public void addDamage(Boss boss, Player player, double damage) {
        Map<UUID, BossDamager> damagers = boss.getDamagers();
        if(!damagers.containsKey(player.getUniqueId())) {
            BossDamager damager = new BossDamager(player.getUniqueId(), player.getName());
            damager.setDamage(damage);
            damagers.put(player.getUniqueId(), damager);
        } else {
            BossDamager damager = damagers.get(player.getUniqueId());
            damager.setDamage(damager.getDamage() + damage);
        }
    }

    public void handleBossDeath(Boss boss, String displayName) {
        if(!boss.getDamagers().isEmpty()) {
            if(boss.getLives() >= 2) {
                boss.setLives(boss.getLives() - 1);
                return;
            }
            boss.setLives(boss.getMaxLives());
            final long baseDamage = Long.parseLong(configStore.getProperty("warzone.boss.base.damage"));

            Map<UUID, BossDamager> damagers = boss.getDamagers();
            List<BossDamager> sorted = new ArrayList<>(damagers.values()).stream().sorted((o1, o2) -> Double.compare(o2.getDamage(), o1.getDamage())).limit(10).toList();
            double balance = game.getConfigNodeFile().getDouble("Boss." + boss.getId() + ".Start balance", 60000);
            double reducePerPlacement = game.getConfigNodeFile().getDouble("Boss." + boss.getId() + ".Reduce per placement", 65000);

            final String arrow = PrismarinConstants.ARROW_RIGHT + " ";
            final String dot = "    " + PrismarinConstants.DOT + " ";
            for(Player online : Bukkit.getOnlinePlayers()) {
                if(game.getWarzoneService().isInWarzone(online) || damagers.containsKey(online.getUniqueId())) {
                    online.sendMessage(" ");
                    online.sendMessage(PrismarinConstants.BORDER);
                    online.sendMessage(" ");
                    online.sendMessage(arrow + "§b" + displayName + " §7has been killed");
                    online.sendMessage(" ");
                    online.sendMessage(arrow + "§bRewards§8:");
                    for (int i = 0; i < sorted.size(); i++) {
                        BossDamager damager = sorted.get(i);
                        if(damager.getDamage() >= baseDamage) {
                            double receiveMoney = balance - (reducePerPlacement * i);
                            final String place = i == 0 ? "§e§l1st" : i == 1 ? "§7§l2nd" : i == 2 ? "§6§l3rd" : "§2" + (i + 1);
                            online.sendMessage(dot + place + " " + damager.getName() + " §8| §c"+Math.round(damager.getDamage())+" damage §8| §a+" + NumberFormatter.formatDoubleToThousands(receiveMoney) + "$ ");
                        }
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
                if(damager.getDamage() < baseDamage) {
                    continue;
                }
                if(boss.getId().equalsIgnoreCase("zahar")) {
                    missionWrapper.getMissionProvider().addProgress(damager.getPlayer(), "killanderson10", 1, 1);
                } else if(boss.getId().equalsIgnoreCase("zaku")) {
                    missionWrapper.getMissionProvider().addProgress(damager.getPlayer(), "killzaku10", 1, 1);
                }
                double receiveMoney = balance - (reducePerPlacement * i);

                User user = userProvider.getUserByUUID(damager.getUuid());
                user.getSeasonData().setBalance(user.getSeasonData().getBalance() + receiveMoney);
                userProvider.saveAsync(user, true);
                if(clanProvider.isInClan(damager.getUuid())) {
                    Clan clan = clanProvider.getClanByPlayer(damager.getUuid());
                    if(i == 0) {
                        clanProvider.giveBuff(clan, boss.getBuff());
                        clanProvider.sendPrefixMessage(clan, "§7Your clan received the buff §e" + boss.getBuff().getDisplayName() + " §7provided by §6" + damager.getName() + " §7for killing the boss §3" + displayName);
                    }
                    game.getWarzoneService().getClanStatsProvider().addBossFights(damager.getUuid());

                }
            }
            boss.getDamagers().clear();
        }
    }


    public boolean existsBossById(String id) {
        return getBossById(id) != null;
    }

    public Boss getBossById(String id) {
        for(Boss boss : bosses) {
            if(boss.getMythicMobs().containsKey(id.toLowerCase())) {
                return boss;
            }
        }
        return null;
    }

}
