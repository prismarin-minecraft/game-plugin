package in.prismar.game.ffa.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class FFAMapPlayer {

    private final Player player;

    @Setter
    private int kills;

    @Setter
    private int deaths;

    private final ItemStack[] content;

    private final ItemStack[] armor;

    public FFAMapPlayer(Player player) {
        this.player = player;
        this.content = player.getInventory().getStorageContents();
        this.armor = player.getInventory().getArmorContents();
    }
}
