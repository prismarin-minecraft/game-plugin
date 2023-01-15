package in.prismar.game.ffa.powerup.impl;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
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
public class AmmoPowerUp implements PowerUp {

    private final Game game;

    public AmmoPowerUp(Game game) {
        this.game = game;
    }

    @Override
    public void onPickUp(Player player) {
        player.sendMessage(PrismarinConstants.PREFIX + "§7You picked up a §aAmmo §7powerup");
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 2);
        game.getMapFacade().getArsenalService().fillAmmo(player);
    }

    @Override
    public String getDisplayName() {
        return "§9Ammo";
    }

    @Override
    public Material getMaterial() {
        return Material.BLUE_WOOL;
    }

    @Override
    public long getRespawnTime() {
        return 1000 * 30;
    }
}
