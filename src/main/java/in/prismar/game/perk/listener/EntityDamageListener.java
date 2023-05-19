package in.prismar.game.perk.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.game.Game;
import in.prismar.game.item.impl.misc.UAVItem;
import in.prismar.game.perk.Perk;
import in.prismar.game.perk.PerkService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class EntityDamageListener implements Listener {

    @Inject
    private Game game;

    @Inject
    private PerkService service;

    private ScoreboardProvider scoreboardProvider;

    public EntityDamageListener() {
        this.scoreboardProvider = PrismarinApi.getProvider(ScoreboardProvider.class);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player player) {
            if(service.hasPerkAndAllowedToUse(player, Perk.ESCAPE)) {
                User user = service.getUserProvider().getUserByUUID(player.getUniqueId());
                if(user.isLocalTimestampAvailable(Perk.ESCAPE.name(), System.currentTimeMillis() + 1000 * 8)) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 40, 1, false, false));
                }
            } else if(service.hasPerkAndAllowedToUse(player, Perk.DEADEYE)) {
                User user = service.getUserProvider().getUserByUUID(player.getUniqueId());
                if(user.isLocalTimestampAvailable(Perk.ESCAPE.name(), System.currentTimeMillis() + 1000 * 4) && !UAVItem.UAV_ACTIVE.contains(player.getUniqueId())) {
                    try {
                        game.getGlowingEntities().setGlowing(event.getDamager(), player, ChatColor.RED);
                    }catch (Exception ex) {}
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {

                                game.getGlowingEntities().unsetGlowing(event.getDamager(), player);
                                scoreboardProvider.recreateTablist(player);
                            } catch (ReflectiveOperationException e) {}
                        }
                    }.runTaskLater(game, 20 * 2);

                }
            } else if(service.hasPerkAndAllowedToUse(player, Perk.FASTHANDS)) {
                double damage = (event.getDamage() / 100.0) * 20;
                event.setDamage(event.getDamage() + damage);
            } else if(service.hasPerkAndAllowedToUse(player, Perk.FORTIFY)) {
                double damage = (event.getDamage() / 100.0) * 20;
                double next = event.getDamage() - damage;
                if(next < 0) {
                    next = 0;
                }
                event.setDamage(next);
            }
        }
    }
}
