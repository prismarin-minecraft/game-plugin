package in.prismar.game.item.reader.impl;

import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.melee.MeleeItem;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.reader.CustomItemReaderSource;
import in.prismar.library.file.toml.TomlConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FileMeleeReaderSource implements CustomItemReaderSource {
    @Override
    public List<CustomItem> read(CustomItemRegistry registry) {
        List<CustomItem> items = new ArrayList<>();
        File directory = new File(registry.getGame().getDefaultDirectory() + "items" + File.separator + "melee" + File.separator);
        for (File file : directory.listFiles()) {
            TomlConfig<MeleeData> tomlConfig = new TomlConfig<>(file.getAbsolutePath(), MeleeData.class);
            if (tomlConfig.getEntity() != null) {
                MeleeData data = tomlConfig.getEntity();
                MeleeItem meleeItem = new MeleeItem(data.getId(), data.getMaterial(), data.getDisplayName());
                meleeItem.setCustomModelData(data.getCustomModelData());
                meleeItem.setPreviewImage(data.getPreviewImage());
                meleeItem.setSkins(data.getSkins());
                meleeItem.setDamage(data.getDamage());
                meleeItem.setAttackSpeed(data.getAttackSpeed());

                meleeItem.generateDefaultLore();
                items.add(meleeItem);
            }
        }

        return items;
    }
}
