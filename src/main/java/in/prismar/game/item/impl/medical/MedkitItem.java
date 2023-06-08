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
public class MedkitItem extends MedicalItem {
    public MedkitItem() {
        super("Medkit", Material.FEATHER, "§4Medkit");
        setCustomModelData(3);
        setHealingTicks(100);
        setHealing(20);
    }

    @Override
    public void onStartHealingSound(Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.7f, 0.1f);
    }

    @Override
    public void onStopHealingSound(Player player) {
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.7f, 2f);
    }
}
