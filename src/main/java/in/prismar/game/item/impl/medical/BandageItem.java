package in.prismar.game.item.impl.medical;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BandageItem extends MedicalItem {
    public BandageItem() {
        super("Bandage", Material.FEATHER, "§fBandage");
        setCustomModelData(2);
        setHealingTicks(60);
        setHealing(4);
    }

    @Override
    public void onStartHealingSound(Player player) {
        player.playSound(player.getLocation(), "medical.bandage", 0.7f, 1.1f);
    }

    @Override
    public void onStopHealingSound(Player player) {

    }

    @Override
    public void onAbort(Player player) {
        player.stopSound("medical.bandage");
    }
}
