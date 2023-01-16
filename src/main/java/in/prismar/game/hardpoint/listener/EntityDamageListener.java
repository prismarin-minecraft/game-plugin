package in.prismar.game.hardpoint.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.ffa.model.GameMap;
import in.prismar.game.ffa.model.GameMapPlayer;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.game.hardpoint.session.HardpointSessionPlayer;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
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
    private HardpointFacade facade;

    @Inject
    private GameStatsDistributor statsDistributor;


    private Map<UUID, Player> lastDamager;

    public EntityDamageListener() {
        this.lastDamager = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player target && event.getDamager() instanceof Player damager) {
            if (facade.isCurrentlyPlaying(target) && facade.isCurrentlyPlaying(damager)) {
                HardpointTeam targetTeam = facade.getTeamByPlayer(target);
                HardpointTeam damagerTeam = facade.getTeamByPlayer(damager);
                if(targetTeam == damagerTeam) {
                    event.setCancelled(true);
                    return;
                }
                this.lastDamager.put(target.getUniqueId(), damager);
                double damage = event.getDamage();
                double nextHealth = target.getHealth() - damage;
                if (nextHealth <= 0) {
                    event.setCancelled(true);
                    handleDeath(target, damager);
                }
            }

        }
    }

    private void handleDeath(Player target, Player damager) {
        target.sendTitle("§4You died", "", 5, 20, 5);
        facade.respawn(target);

        if (damager != null) {
            int health = (int) damager.getHealth();
            facade.sendMessage(PrismarinConstants.PREFIX + statsDistributor.getRandomDeathMessage(damager, target) + " §8(§c" + health + "♥§8)");
            damager.setHealth(20);
        } else {
            facade.sendMessage(PrismarinConstants.PREFIX + "§c" + target.getName() + " §7just died.");

        }


        statsDistributor.resetKillstreak(target);

        boolean samePlayer = damager == null ? true : damager.getUniqueId().equals(target.getUniqueId());
        if (!samePlayer) {
            int streak = statsDistributor.addKillstreak(damager);
            StringBuilder skulls = new StringBuilder();
            for (int i = 0; i < streak; i++) {
                if (i <= 4) {
                    skulls = skulls.append("§4☠ ");
                }
            }

            damager.sendTitle(skulls.toString().trim(), "", 5, 20, 5);
        }

        HardpointSessionPlayer targetMapPlayer = facade.getSessionPlayerByPlayer(target);

        if (!samePlayer) {
            facade.getArsenalService().fillAmmo(damager);
            HardpointSessionPlayer damagerMapPlayer = facade.getSessionPlayerByPlayer(damager);
            damagerMapPlayer.setKills(damagerMapPlayer.getKills() + 1);
            statsDistributor.addKill(damager);
            int exp = statsDistributor.addFFABattlePassEXP(damager);
            statsDistributor.addHardpointKill(damager);

            int money = statsDistributor.addFFAKillMoney(damager);

            damager.sendMessage(PrismarinConstants.PREFIX + "§7You received §a" + NumberFormatter.formatNumberToThousands(exp) + " EXP §7for killing §c" + target.getName());
            damager.sendMessage(PrismarinConstants.PREFIX + "§7You received §6" + NumberFormatter.formatNumberToThousands(money) + "$ §7for killing §c" + target.getName());

            int killstreak = statsDistributor.getKillstreak(damager);
            int streakMoney = statsDistributor.addFFAKillstreakMoney(damager, killstreak);
            if (streakMoney != -1) {
                damager.sendMessage(PrismarinConstants.PREFIX + "§7You received §6" + NumberFormatter.formatNumberToThousands(streakMoney) + "$ §7for having a §c" + killstreak + " §7killstreak");
            }

            if (killstreak == 10) {
                damager.getInventory().addItem(facade.getItemRegistry().createItem("Airstrike"));
            }

            displayStreak(damager, killstreak);

        }

        targetMapPlayer.setDeaths(targetMapPlayer.getDeaths() + 1);

        statsDistributor.addDeath(target);
        statsDistributor.addHardpointDeath(target);

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (facade.isCurrentlyPlaying(player)) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                    double damage = 6;
                    double nextHealth = player.getHealth() - damage;
                    if (nextHealth <= 0.0) {
                        player.setFireTicks(1);
                        event.setCancelled(true);
                        handleDeath(player, lastDamager.containsKey(player.getUniqueId()) ? lastDamager.get(player.getUniqueId()) : null);
                    } else {
                        event.setDamage(damage);
                    }
                    return;
                }
                if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setCancelled(true);
                }
            }

        }
    }

    private void displayStreak(Player player, int streak) {
        statsDistributor.displayStreak(player, streak, chosenMessage -> {
            for(HardpointTeam team : facade.getSession().getPlayers().keySet()) {
                for(HardpointSessionPlayer sessionPlayer : facade.getSession().getPlayers().get(team).values()) {
                    sessionPlayer.getPlayer().sendMessage(" ");
                    sessionPlayer.getPlayer().sendMessage(PrismarinConstants.PREFIX + chosenMessage);
                    sessionPlayer.getPlayer().sendMessage(" ");
                }
            }
        });
    }


}
