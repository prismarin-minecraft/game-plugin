package in.prismar.game.item.reader.impl;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.fishing.FishingRodItem;
import in.prismar.game.item.impl.melee.MeleeAttackSpeed;
import in.prismar.game.item.impl.melee.MeleeItem;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.model.Skin;
import in.prismar.game.item.reader.CustomItemReaderSource;
import in.prismar.game.web.config.builder.ConfigNodeBuilder;
import in.prismar.game.web.config.file.ConfigNodeFile;
import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.config.model.ConfigNodeType;
import in.prismar.library.common.EnumUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class ConfigFishingRodReaderSource implements CustomItemReaderSource {

    private final Game game;
    @Override
    public List<CustomItem> read(CustomItemRegistry registry) {
        List<CustomItem> items = new ArrayList<>();
        ConfigNodeFile file = game.getConfigNodeFile();
        ConfigNode root = file.getNode("fishing_rods");
        for(ConfigNode node : root.getChildren().values()) {
            FishingRodItem rod = new FishingRodItem(file.getString(node, "ID"),
                    file.getString(node, "Display name").replace("&", "§"));
            rod.setCustomModelData(file.getInteger(node, "Custom model data"));
            rod.setMinWaitTimeSeconds(file.getInteger(node, "Min wait time seconds"));
            rod.setMaxWaitTimeSeconds(file.getInteger(node, "Max wait time seconds"));

            items.add(rod);
        }
        return items;
    }

    @Override
    public void load() {
        ConfigNodeFile file = game.getConfigNodeFile();
        ConfigNode root = file.getOrCreateRootNode("Fishing_Rods");

        final String templateId = "fishing_rods";
        if(!file.getTemplateFile().existsTemplate(templateId)) {
            ConfigNodeBuilder template = new ConfigNodeBuilder("Fishing_Rod", ConfigNodeType.CATEGORY);
            template.addChild(new ConfigNodeBuilder("ID", ConfigNodeType.ENTRY).setValue("fishringrod1"));
            template.addChild(new ConfigNodeBuilder("Display name", ConfigNodeType.ENTRY).setValue("&6Fishing Rod"));
            template.addChild(new ConfigNodeBuilder("Custom model data", ConfigNodeType.ENTRY).setValue(1));
            template.addChild(new ConfigNodeBuilder("Min wait time seconds", ConfigNodeType.ENTRY).setValue(8));
            template.addChild(new ConfigNodeBuilder("Max wait time seconds", ConfigNodeType.ENTRY).setValue(10));

            file.getTemplateFile().registerTemplate(templateId, template.build());

            root.setTemplate(templateId);
        }


        file.saveDelayed();
    }

}
