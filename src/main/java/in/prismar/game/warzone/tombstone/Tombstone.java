package in.prismar.game.warzone.tombstone;

import in.prismar.library.spigot.hologram.Hologram;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class Tombstone {

    private long despawnTimestamp;
    private Inventory inventory;
    private Hologram hologram;
}
