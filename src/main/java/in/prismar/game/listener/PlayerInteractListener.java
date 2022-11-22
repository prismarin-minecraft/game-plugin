package in.prismar.game.listener;

import in.prismar.game.Game;
import in.prismar.game.item.gun.Bullet;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerInteractListener implements Listener {

    long lastInteract;
    int fireRate = 1080;

    long currentTicks = 0;

    double spread = 10;

    public PlayerInteractListener(Game game) {
        Bukkit.getScheduler().runTaskTimer(game, () -> {
            int fireTicks = 20 * 60 / fireRate;
            for (Player player : Bukkit.getOnlinePlayers()) {
                long difference = System.currentTimeMillis() - lastInteract;
                if (difference < 200 && currentTicks % fireTicks == 0) {
                    Vector dir = player.getLocation().getDirection().normalize();
                    double spread = player.isSneaking() ? this.spread / 2 : this.spread;
                    dir.setX(dir.getX() + getRandFactor(spread));
                    dir.setY(dir.getY() + getRandFactor(spread));
                    dir.setZ(dir.getZ() + getRandFactor(spread));
                    new Bullet(Particle.CRIT, player.getEyeLocation().subtract(0, 0.3, 0),
                            dir, 20).invoke();



                }

            }
            currentTicks++;
        }, 1, 1);
    }

    private double getRandFactor(double spread) {
        return MathUtil.randomDouble(-0.01, 0.01) * spread;
    }

    @EventHandler
    public void onCall(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem() != null) {
            if (event.getItem().getType() == Material.STICK) {
                Player player = event.getPlayer();
                lastInteract = System.currentTimeMillis();


            }
        }
    }

}
