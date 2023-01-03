package in.prismar.game.ffa.listener.entity;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.ffa.model.GameMap;
import in.prismar.game.ffa.model.GameMapPlayer;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.common.math.MathUtil;
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
    private GameMapFacade facade;

    @Inject
    private GameStatsDistributor statsDistributor;

    private ConfigStore store;

    private Map<UUID, Player> lastDamager;

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
        target.sendTitle("§4You died", "", 5, 20, 5);
        facade.respawn(target);

        if(damager != null) {
            int health = (int)damager.getHealth();
            facade.sendMessage(PrismarinConstants.PREFIX + getRandomDeathMessage(damager, target) + " §8(§c"+health+"♥§8)");
            damager.setHealth(20);
        } else {
            facade.sendMessage(PrismarinConstants.PREFIX + "§c" + target.getName() + " §7just died.");

        }


        statsDistributor.resetKillstreak(target);

        boolean samePlayer = damager == null ? true : damager.getUniqueId().equals(target.getUniqueId());
        if(!samePlayer) {
            int streak = statsDistributor.addKillstreak(damager);
            StringBuilder skulls = new StringBuilder();
            for (int i = 0; i < streak; i++) {
                if(i <= 4) {
                    skulls = skulls.append("§4☠ ");
                }
            }

            damager.sendTitle(skulls.toString().trim(), "", 5, 20, 5);
        }


        Optional<GameMap> mapOptional = facade.getMapByPlayer(target);
        if(mapOptional.isPresent()) {
            GameMap map = mapOptional.get();
            GameMapPlayer targetMapPlayer = map.getPlayers().get(target.getUniqueId());

            if(!samePlayer) {
                GameMapPlayer damagerMapPlayer = map.getPlayers().get(damager.getUniqueId());
                damagerMapPlayer.setKills(damagerMapPlayer.getKills() + 1);
                statsDistributor.addKill(damager);
                statsDistributor.addMapKill(damager, map.getId());
                displayStreak(map, damager, statsDistributor.getKillstreak(damager));
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
                    double damage = 4;
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

    private void displayStreak(GameMap map, Player player, int streak) {
        String[] messages = store.getProperty("killstreak.messages").split("/");
        String chosenMessage = null;
        for(String message : messages) {
            if(message.startsWith("{" + streak)) {
                chosenMessage = message;
                break;
            }
        }
        if(chosenMessage != null) {
            chosenMessage = chosenMessage.replace("{" + streak + "}", "")
                    .replace("{player}", player.getName()).replace("&", "§");
            for(GameMapPlayer mapPlayer : map.getPlayers().values()) {
                mapPlayer.getPlayer().sendMessage(" ");
                mapPlayer.getPlayer().sendMessage(PrismarinConstants.PREFIX + chosenMessage);
                mapPlayer.getPlayer().sendMessage(" ");
            }
        }


    }

    public String getRandomDeathMessage(Player killer, Player target) {
        String[] messages = store.getProperty("death.messages").split("/");
        String random = messages[MathUtil.random(messages.length - 1)];
        return random.replace("&", "§").replace("{killer}", killer.getName())
                .replace("{target}", target.getName());
    }

}
