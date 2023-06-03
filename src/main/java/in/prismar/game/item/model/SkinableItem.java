package in.prismar.game.item.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class SkinableItem extends CustomItem {

    private List<Skin> skins;

    public SkinableItem(String id, Material material, String displayName) {
        super(id, material, displayName);
        this.skins = new ArrayList<>();
    }

    public int getSkinDataByItem(ItemStack stack) {
        if(stack.hasItemMeta()) {
            ItemMeta meta = stack.getItemMeta();
            if(meta.hasCustomModelData()) {
                for(Skin skin : skins) {
                    if(skin.getData() == meta.getCustomModelData() || skin.getData() + 1 == meta.getCustomModelData() || skin.getData() + 2 == meta.getCustomModelData()) {
                        return skin.getData();
                    }
                }
            }
        }
        return -1;
    }
}
