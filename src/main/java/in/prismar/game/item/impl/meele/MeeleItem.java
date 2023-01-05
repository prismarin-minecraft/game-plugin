package in.prismar.game.item.impl.meele;

import in.prismar.game.item.CustomItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class MeeleItem extends CustomItem {

    private double damage;

    public MeeleItem(String id, Material material, String displayName) {
        super(id, material, displayName);
    }
}
