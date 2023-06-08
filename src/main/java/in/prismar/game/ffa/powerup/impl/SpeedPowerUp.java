package in.prismar.game.ffa.powerup.impl;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.powerup.PowerUp;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SpeedPowerUp implements PowerUp {

    @Override
    public void onPickUp(Player player) {
        player.sendMessage(PrismarinConstants.PREFIX + "§7You picked up a §fSpeed §7powerup");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 2);

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 15, 0, false, false));
    }

    @Override
    public String getDisplayName() {
        return "§fSpeed";
    }

    @Override
    public int getCustomModelData() {
        return 2;
    }

    @Override
    public long getRespawnTime() {
        return 1000 * 30;
    }
}
