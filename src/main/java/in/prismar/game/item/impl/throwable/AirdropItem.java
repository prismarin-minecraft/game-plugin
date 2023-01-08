package in.prismar.game.item.impl.throwable;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AirdropItem extends ThrowableItem {
    public AirdropItem() {
        super("Airdrop", Material.STICK, "§eAirdrop");
        setCustomModelData(1);
        allFlags();


    }

    @Override
    public boolean isAllowedToThrow(Player player, Game game) {
        if(!game.getExtractionFacade().isIn(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou can only call an airdrop inside extraction.");
            return false;
        }
        return true;
    }

    @Override
    public void onThrow(ThrowEvent throwEvent) {
        Item item = throwEvent.getItem();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(item.isOnGround()) {
                    cancel();
                    item.remove();
                    item.getWorld().playSound(item.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 3f, 1);
                    throwEvent.getGame().getAirDropRegistry().callAirDrop(item.getLocation());
                    return;
                }
                item.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, item.getLocation(), 0);
            }
        }.runTaskTimer(throwEvent.getGame(), 1, 1);
    }




}
