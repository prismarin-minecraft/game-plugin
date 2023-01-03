package in.prismar.game.ffa.powerup.impl;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.ffa.powerup.PowerUp;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DoubleJumpPowerUp implements PowerUp {

    private final UserProvider<User> provider;

    public DoubleJumpPowerUp() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
    }

    @Override
    public void onPickUp(Player player) {
        player.sendMessage(PrismarinConstants.PREFIX + "§7You picked up a §cDouble Jump §7powerup");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 2);
        User user = provider.getUserByUUID(player.getUniqueId());
        user.setTag("doubleJump", System.currentTimeMillis() + 1000 * 15);
    }

    @Override
    public String getDisplayName() {
        return "§cDouble Jump";
    }

    @Override
    public Material getMaterial() {
        return Material.RED_WOOL;
    }

    @Override
    public long getRespawnTime() {
        return 1000 * 30;
    }
}
