package in.prismar.game.hardpoint.session;

import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class HardpointSessionPlayer {

    private Player player;
    private int kills;
    private int deaths;

    private ItemStack[] content;

    private ItemStack[] armor;

    public HardpointSessionPlayer(Player player) {
        this.player = player;
        this.content = player.getInventory().getStorageContents();
        this.armor = player.getInventory().getArmorContents();
    }
}
