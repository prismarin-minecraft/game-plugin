package in.prismar.game.animation.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;


/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@NoArgsConstructor
public class AnimationFrameBlock {

    private Location location;
    private Material material;
    private String blockDataValue;
    private transient BlockData blockData;

    public AnimationFrameBlock(Location location, Material material) {
        this.location = location;
        this.material = material;
        this.blockData = location.getBlock().getBlockData();
        this.blockDataValue = blockData.getAsString();
    }
}
