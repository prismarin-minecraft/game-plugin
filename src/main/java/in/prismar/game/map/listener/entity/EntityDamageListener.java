package in.prismar.game.map.listener.entity;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.map.GameMapFacade;
import in.prismar.game.map.model.GameMap;
import in.prismar.game.map.model.GameMapPlayer;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Optional;

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

    public EntityDamageListener() {
        this.store = PrismarinApi.getProvider(ConfigStore.class);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.isCancelled()) {
            return;
        }
        if(event.getEntity() instanceof Player target && event.getDamager() instanceof Player damager) {
             double damage = event.getDamage();
             double nextHealth = target.getHealth() - damage;
             if(nextHealth <= 0) {
                 statsDistributor.resetKillstreak(target);
                 int streak = statsDistributor.addKillstreak(damager);
                 StringBuilder skulls = new StringBuilder();
                 for (int i = 0; i < streak; i++) {
                     if(i <= 4) {
                         skulls = skulls.append("§4☠ ");
                     }
                 }
                 damager.sendTitle(skulls.toString().trim(), "", 5, 20, 5);
                 target.sendTitle("§4You died", "", 5, 20, 5);
                 event.setCancelled(true);
                 facade.respawn(target);

                 int health = (int)damager.getHealth();
                 facade.sendMessage(PrismarinConstants.PREFIX + getRandomDeathMessage(damager, target) + " §8(§c"+health+"♥§8)");

                 damager.setHealth(20);

                 Optional<GameMap> mapOptional = facade.getMapByPlayer(target);
                 if(mapOptional.isPresent()) {
                     GameMap map = mapOptional.get();
                     GameMapPlayer targetMapPlayer = map.getPlayers().get(target.getUniqueId());
                     GameMapPlayer damagerMapPlayer = map.getPlayers().get(damager.getUniqueId());

                     damagerMapPlayer.setKills(damagerMapPlayer.getKills() + 1);
                     targetMapPlayer.setDeaths(targetMapPlayer.getDeaths() + 1);

                     facade.updateLeaderboard(map);

                     statsDistributor.addKill(damager);
                     statsDistributor.addMapKill(damager, map.getId());

                     statsDistributor.addDeath(target);
                     statsDistributor.addMapDeath(target, map.getId());

                     displayStreak(map, damager, streak);

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

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(facade.isCurrentlyPlaying(player)) {
                if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                    event.setCancelled(true);
                }
            } else {
                event.setCancelled(true);
            }

        }
    }

}
