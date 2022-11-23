package in.prismar.game.map.powerup.impl;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.map.powerup.PowerUp;
import in.prismar.library.common.math.MathUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HealPowerUp implements PowerUp {

    @Override
    public void onPickUp(Player player) {
        player.setHealth(20);
        player.sendMessage(PrismarinConstants.PREFIX + "§7You picked up a §aHeal §7powerup");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 2);
    }

    @Override
    public String getDisplayName() {
        return "§aHeal";
    }

    @Override
    public Material getMaterial() {
        return Material.LIME_WOOL;
    }

    @Override
    public long getRespawnTime() {
        return 1000 * 30;
    }
}
