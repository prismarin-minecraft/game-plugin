package in.prismar.game.ffa.powerup;

import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface PowerUp {

    void onPickUp(Player player);

    String getDisplayName();


    int getCustomModelData();

    long getRespawnTime();
}
