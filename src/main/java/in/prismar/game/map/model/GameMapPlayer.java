package in.prismar.game.map.model;

import lombok.AllArgsConstructor;
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
public class GameMapPlayer {

    private Player player;

    @Setter
    private int kills;

    @Setter
    private int deaths;

    private ItemStack[] content;

    private ItemStack[] armor;

    public GameMapPlayer(Player player) {
        this.player = player;
        this.content = player.getInventory().getStorageContents();
        this.armor = player.getInventory().getArmorContents();
    }
}
