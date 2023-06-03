package in.prismar.game.item.reader.impl;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
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
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
public class ConfigMeleeReaderSource implements CustomItemReaderSource {

    private final Game game;
    @Override
    public List<CustomItem> read(CustomItemRegistry registry) {
        List<CustomItem> items = new ArrayList<>();
        ConfigNodeFile file = game.getConfigNodeFile();
        ConfigNode root = file.getNode("melee");
        for(ConfigNode node : root.getChildren().values()) {
            MeleeItem melee = new MeleeItem(file.getString(node, "ID"),
                    Material.valueOf(file.getString(node, "Material")),
                    file.getString(node, "Display name").replace("&", "§"));
            melee.setCustomModelData(file.getInteger(node, "Custom model data"));
            melee.setDamage(file.getInteger(node, "Damage"));
            melee.setPreviewImage(file.getString(node, "Preview image"));
            melee.setAttackSpeed(MeleeAttackSpeed.valueOf(file.getString(node, "Attack speed")));

            ConfigNode skins = node.getChildren().get("Skins");
            if(!skins.getChildren().isEmpty()) {
                List<Skin> skinList = new ArrayList<>();
                for(ConfigNode skin : skins.getChildren().values()) {
                    Skin itemSkin = new Skin();
                    itemSkin.setData(file.getInteger(skin, "Custom model data"));
                    itemSkin.setDisplayName(file.getString(skin, "Display name").replace("&", "§"));
                    itemSkin.setPreviewImage(file.getString(skin, "Preview image"));
                    skinList.add(itemSkin);
                }
                melee.setSkins(skinList);
            } else {
                melee.setSkins(new ArrayList<>());
            }

            melee.generateDefaultLore();
            items.add(melee);
        }
        return items;
    }

    @Override
    public void load() {
        ConfigNodeFile file = game.getConfigNodeFile();
        ConfigNode root = file.getOrCreateRootNode("Melee");

        final String templateId = "melee";
        if(!file.getTemplateFile().existsTemplate(templateId)) {
            ConfigNodeBuilder template = new ConfigNodeBuilder("Melee", ConfigNodeType.CATEGORY);
            template.addChild(new ConfigNodeBuilder("ID", ConfigNodeType.ENTRY).setValue("example"));
            template.addChild(new ConfigNodeBuilder("Material", ConfigNodeType.ENTRY).setValue(Material.DIAMOND_SWORD.name()));
            template.addChild(new ConfigNodeBuilder("Display name", ConfigNodeType.ENTRY).setValue("&cHello"));
            template.addChild(new ConfigNodeBuilder("Custom model data", ConfigNodeType.ENTRY).setValue(1));
            template.addChild(new ConfigNodeBuilder("Attack speed", ConfigNodeType.ENTRY).setValue(MeleeAttackSpeed.NORMAL.name()).addOptions(EnumUtil.getEnumNames(MeleeAttackSpeed.values())));
            template.addChild(new ConfigNodeBuilder("Damage", ConfigNodeType.ENTRY).setValue(8));
            template.addChild(new ConfigNodeBuilder("Preview image", ConfigNodeType.ENTRY).setValue("https://example.com/image"));

            ConfigNodeBuilder skins = new ConfigNodeBuilder("Skins", ConfigNodeType.CATEGORY);
            skins.setTemplate("items_skin");
            template.addChild(skins);

            file.getTemplateFile().registerTemplate(templateId, template.build());

            root.setTemplate(templateId);
        }


        file.saveDelayed();
    }

}
