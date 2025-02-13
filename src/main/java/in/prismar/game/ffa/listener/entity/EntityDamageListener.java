package in.prismar.game.ffa.listener.entity;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.ffa.model.FFAMap;
import in.prismar.game.ffa.model.FFAMapPlayer;
import in.prismar.game.missions.MissionWrapper;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageListener implements Listener {

    @Inject
    private FFAFacade facade;

    @Inject
    private GameStatsDistributor statsDistributor;



    private final ConfigStore store;

    private final Map<UUID, Player> lastDamager;

    public EntityDamageListener() {
        this.store = PrismarinApi.getProvider(ConfigStore.class);
        this.lastDamager = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getEntity() instanceof Player target && event.getDamager() instanceof Player damager) {
            if(facade.isCurrentlyPlaying(target) && facade.isCurrentlyPlaying(damager)) {
                this.lastDamager.put(target.getUniqueId(), damager);
                double damage = event.getDamage();
                double nextHealth = target.getHealth() - damage;
                if(nextHealth <= 0) {
                    event.setCancelled(true);
                    handleDeath(target, damager);
                }
            }

        }
    }

    private void handleDeath(Player target, Player damager) {
        MissionWrapper.progress(damager, "kill_5_players_in_ffa", 1, 1);
        target.getPlayer().playSound(target.getLocation(), "game.death", 1f, 1);
        target.sendTitle("§4You died", "", 5, 20, 5);
        facade.respawn(target);


        statsDistributor.resetKillstreak(target);

        boolean samePlayer = damager == null || damager.getUniqueId().equals(target.getUniqueId());
        if(!samePlayer) {
            int health = (int)damager.getHealth();
            facade.sendMessage(PrismarinConstants.PREFIX + statsDistributor.getRandomDeathMessage(damager, target) + " §8(§c"+health+"♥§8)");
            damager.setHealth(20);
            int streak = statsDistributor.addKillstreak(damager);
            StringBuilder skulls = new StringBuilder();
            for (int i = 0; i < streak; i++) {
                if(i <= 4) {
                    skulls = skulls.append("§4☠ ");
                }
            }

            damager.sendTitle(skulls.toString().trim(), "", 5, 20, 5);
        } else {
            facade.sendMessage(PrismarinConstants.PREFIX + "§c" + target.getName() + " §7just died.");
        }

        lastDamager.remove(target.getUniqueId());


        Optional<FFAMap> mapOptional = facade.getMapByPlayer(target);
        if(mapOptional.isPresent()) {
            FFAMap map = mapOptional.get();
            FFAMapPlayer targetMapPlayer = map.getPlayers().get(target.getUniqueId());

            if(!samePlayer) {
                FFAMapPlayer damagerMapPlayer = map.getPlayers().get(damager.getUniqueId());
                damagerMapPlayer.setKills(damagerMapPlayer.getKills() + 1);
                statsDistributor.addKill(damager);
                int exp = statsDistributor.addFFABattlePassEXP(damager);
                statsDistributor.addMapKill(damager, map.getId());

                int money = statsDistributor.addFFAKillMoney(damager);

                damager.sendMessage(PrismarinConstants.PREFIX + "§7You received §a" + NumberFormatter.formatNumberToThousands(exp) + " EXP §7for killing §c" + target.getName());
                damager.sendMessage(PrismarinConstants.PREFIX + "§7You received §6" + NumberFormatter.formatNumberToThousands(money) + "$ §7for killing §c" + target.getName());

                int killstreak = statsDistributor.getKillstreak(damager);
                int streakMoney = statsDistributor.addFFAKillstreakMoney(damager, killstreak);
                if(streakMoney != -1) {
                    damager.sendMessage(PrismarinConstants.PREFIX + "§7You received §6" + NumberFormatter.formatNumberToThousands(streakMoney) + "$ §7for having a §c" + killstreak + " §7killstreak");
                }

                if(killstreak == 10) {
                    damager.sendTitle("§c", "§c+ Airstrike", 10, 20, 10);
                    damager.getInventory().addItem(facade.getItemRegistry().createItem("Airstrike"));
                    damager.getPlayer().playSound(damager.getLocation(), Sound.ITEM_TOTEM_USE, 0.45F, 1);
                } else if(killstreak == 20) {
                    damager.sendTitle("§c", "§4§l+ Raygun", 10, 20, 10);
                    damager.getInventory().addItem(facade.getItemRegistry().createItem("Raygun"));
                    damager.getPlayer().playSound(damager.getLocation(), Sound.ITEM_TOTEM_USE, 0.45F, 1);
                } else if(killstreak == 5) {
                    damager.sendTitle("§a", "§a+ UAV", 10, 20, 10);
                    damager.getInventory().addItem(facade.getItemRegistry().createItem("UAV"));
                    damager.getPlayer().playSound(damager.getLocation(), Sound.ITEM_TOTEM_USE, 0.45F, 1);
                }

                displayStreak(map, damager, killstreak);


                facade.updateLeaderboard(map);
            }

            targetMapPlayer.setDeaths(targetMapPlayer.getDeaths() + 1);

            statsDistributor.addDeath(target);
            statsDistributor.addMapDeath(target, map.getId());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(facade.isCurrentlyPlaying(player)) {
                if(event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                    double damage = 6;
                    double nextHealth = player.getHealth() - damage;
                    if(nextHealth <= 0.0) {
                        player.setFireTicks(1);
                        event.setCancelled(true);
                        handleDeath(player, lastDamager.containsKey(player.getUniqueId()) ? lastDamager.get(player.getUniqueId()) : null) ;
                    } else {
                        event.setDamage(damage);
                    }
                    return;
                }
                if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setCancelled(true);
                }
            }

        }
    }

    private void displayStreak(FFAMap map, Player player, int streak) {
        statsDistributor.displayStreak(player, streak, chosenMessage -> {
            for(FFAMapPlayer mapPlayer : map.getPlayers().values()) {
                mapPlayer.getPlayer().sendMessage(" ");
                mapPlayer.getPlayer().sendMessage(PrismarinConstants.PREFIX + chosenMessage);
                mapPlayer.getPlayer().sendMessage(" ");
            }
        });
    }


}
