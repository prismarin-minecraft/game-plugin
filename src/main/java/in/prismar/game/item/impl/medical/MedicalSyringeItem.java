package in.prismar.game.item.impl.medical;

import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class MedicalSyringeItem extends MedicalItem {
    public MedicalSyringeItem() {
        super("MedicalSyringe", Material.FEATHER, "§cMedical Syringe");
        setHealingTicks(30);
        setCustomModelData(1);
    }
}
