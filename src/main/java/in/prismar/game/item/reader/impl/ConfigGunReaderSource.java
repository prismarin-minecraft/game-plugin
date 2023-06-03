package in.prismar.game.item.reader.impl;

import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.model.Skin;
import in.prismar.game.item.reader.CustomItemReaderSource;
import in.prismar.game.web.config.builder.ConfigNodeBuilder;
import in.prismar.game.web.config.file.ConfigNodeFile;
import in.prismar.game.web.config.file.ConfigTemplateFile;
import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.config.model.ConfigNodeType;
import in.prismar.library.common.EnumUtil;
import in.prismar.library.meta.anno.Config;
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
public class ConfigGunReaderSource implements CustomItemReaderSource {

    private final Game game;


    @Override
    public List<CustomItem> read(CustomItemRegistry registry) {
        List<CustomItem> items = new ArrayList<>();
        ConfigNodeFile file = game.getConfigNodeFile();
        ConfigNode root = file.getNode("guns");
        for(ConfigNode category : root.getChildren().values()) {
            for(ConfigNode node : category.getChildren().values()) {
                Gun gun = new Gun(file.getString(node, "ID"),
                        GunType.getGunTypeByDisplayName(category.getName()),
                        Material.valueOf(file.getString(node, "Material")),
                        file.getString(node, "Display name").replace("&", "§"));
                gun.setCustomModelData(file.getInteger(node, "Custom model data"));
                gun.setShootParticle(Particle.valueOf(file.getString(node, "Shoot particle")));
                gun.setPreviewImage(file.getString(node, "Preview image"));
                gun.setAmmoType(AmmoType.valueOf(file.getString(node, "Ammo type")));
                gun.setRange(file.getDouble(node, "Range"));
                gun.setFireRate(file.getInteger(node, "Fire rate"));
                gun.setSpread(file.getDouble(node, "Spread"));
                gun.setSneakSpread(file.getDouble(node, "Sneak spread"));
                gun.setBulletsPerShot(file.getInteger(node, "Bullets per shot"));
                gun.setMaxAmmo(file.getInteger(node, "Max ammo"));
                gun.setReloadTimeInTicks(file.getInteger(node, "Reload time in ticks"));
                gun.setHeadDamage(file.getDouble(node, "Head damage"));
                gun.setBodyDamage(file.getDouble(node, "Body damage"));
                gun.setLegDamage(file.getDouble(node, "Leg damage"));
                gun.setZoom(file.getInteger(node, "Zoom"));

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
                    gun.setSkins(skinList);
                } else {
                    gun.setSkins(new ArrayList<>());
                }
                ConfigNode sounds = node.getChildren().get("Sounds");
                if(!sounds.getChildren().isEmpty()) {
                    for(ConfigNode sound : sounds.getChildren().values()) {

                        GunSound gunSound = new GunSound(null, (float)file.getDouble(sound, "Volume"), (float)file.getDouble(sound, "Pitch"));
                        gunSound.setSoundName(file.getString(sound, "Sound"));
                        gunSound.setSurroundingDistance(file.getDouble(sound, "Surrounding distance"));
                        gun.getSounds().put(GunSoundType.valueOf(sound.getName()), gunSound);
                    }
                }
                gun.generateDefaultLore();
                gun.buildStateItems();;
                items.add(gun);
            }
        }
        return items;
    }

    @Override
    public void load() {
        ConfigNodeFile file = game.getConfigNodeFile();
        ConfigTemplateFile templateFile = file.getTemplateFile();
        ConfigNode root = file.getOrCreateRootNode("Guns");
        for(GunType type : GunType.values()) {
            final String name = type.getDisplayName().replace(" ", "_");
            if(!root.getChildren().containsKey(name)) {
                ConfigNodeBuilder category = new ConfigNodeBuilder(name, ConfigNodeType.CATEGORY);
                final String templateId = "guns_" + type.name().toLowerCase();
                final String skinsTemplateId = "items_skin";
                final String soundsTemplateId = "items_sound";
                if(!templateFile.existsTemplate(templateId)) {
                    ConfigNodeBuilder template = new ConfigNodeBuilder(name, ConfigNodeType.CATEGORY);
                    template.addChild(new ConfigNodeBuilder("ID", ConfigNodeType.ENTRY).setValue("example"));
                    template.addChild(new ConfigNodeBuilder("Material", ConfigNodeType.ENTRY).setValue(type.getMaterial().name()));
                    template.addChild(new ConfigNodeBuilder("Display name", ConfigNodeType.ENTRY).setValue("&cHello"));
                    template.addChild(new ConfigNodeBuilder("Custom model data", ConfigNodeType.ENTRY).setValue(1));
                    template.addChild(new ConfigNodeBuilder("Ammo type", ConfigNodeType.ENTRY).setValue(type.name()).addOptions(EnumUtil.getEnumNames(AmmoType.values())));
                    template.addChild(new ConfigNodeBuilder("Shoot particle", ConfigNodeType.ENTRY).setValue(Particle.CRIT.name()));
                    template.addChild(new ConfigNodeBuilder("Range", ConfigNodeType.ENTRY).setValue(30.0));
                    template.addChild(new ConfigNodeBuilder("Fire rate", ConfigNodeType.ENTRY).setValue(100));
                    template.addChild(new ConfigNodeBuilder("Spread", ConfigNodeType.ENTRY).setValue(4.0));
                    template.addChild(new ConfigNodeBuilder("Sneak spread", ConfigNodeType.ENTRY).setValue(1.0));
                    template.addChild(new ConfigNodeBuilder("Bullets per shot", ConfigNodeType.ENTRY).setValue(1));
                    template.addChild(new ConfigNodeBuilder("Max ammo", ConfigNodeType.ENTRY).setValue(10));
                    template.addChild(new ConfigNodeBuilder("Reload time in ticks", ConfigNodeType.ENTRY).setValue(10));
                    template.addChild(new ConfigNodeBuilder("Head damage", ConfigNodeType.ENTRY).setValue(8.0));
                    template.addChild(new ConfigNodeBuilder("Body damage", ConfigNodeType.ENTRY).setValue(6.0));
                    template.addChild(new ConfigNodeBuilder("Leg damage", ConfigNodeType.ENTRY).setValue(3.0));
                    template.addChild(new ConfigNodeBuilder("Zoom", ConfigNodeType.ENTRY).setValue(1));
                    template.addChild(new ConfigNodeBuilder("Preview image", ConfigNodeType.ENTRY).setValue("https://example.com/image"));
                    ConfigNodeBuilder skins = new ConfigNodeBuilder("Skins", ConfigNodeType.CATEGORY);
                    skins.setTemplate(skinsTemplateId);
                    template.addChild(skins);

                    ConfigNodeBuilder sounds = new ConfigNodeBuilder("Sounds", ConfigNodeType.CATEGORY);
                    sounds.setTemplate(soundsTemplateId);
                    template.addChild(sounds);


                    templateFile.registerTemplate(templateId, template.build());
                }

                if(!templateFile.existsTemplate(skinsTemplateId)) {
                    ConfigNodeBuilder skinsTemplate = new ConfigNodeBuilder("Skin", ConfigNodeType.CATEGORY);
                    skinsTemplate.addChild(new ConfigNodeBuilder("Display name", ConfigNodeType.ENTRY).setValue("&dRainbow"));
                    skinsTemplate.addChild(new ConfigNodeBuilder("Preview image", ConfigNodeType.ENTRY).setValue("https://example.com/image"));
                    skinsTemplate.addChild(new ConfigNodeBuilder("Custom model data", ConfigNodeType.ENTRY).setValue(1));
                    skinsTemplate.setTemplate(skinsTemplateId);
                    templateFile.registerTemplate(skinsTemplateId, skinsTemplate.build());
                }

                if(!templateFile.existsTemplate(soundsTemplateId)) {
                    ConfigNode soundsTemplate = new ConfigNodeBuilder(type.name(), ConfigNodeType.CATEGORY)
                            .addChild(new ConfigNodeBuilder("Sound", ConfigNodeType.ENTRY).setValue("RELOAD_IN"))
                            .addChild(new ConfigNodeBuilder("Volume", ConfigNodeType.ENTRY).setValue(1.0))
                            .addChild(new ConfigNodeBuilder("Pitch", ConfigNodeType.ENTRY).setValue(1.0))
                            .addChild(new ConfigNodeBuilder("Surrounding distance", ConfigNodeType.ENTRY).setValue(15.0))
                            .setTemplate(soundsTemplateId)
                            .build();
                    templateFile.registerTemplate(soundsTemplateId, soundsTemplate);
                }
                category.setTemplate(templateId);
                root.getChildren().put(name, category.build());
            }
        }
        file.saveDelayed();
    }

    private ConfigNode createSound(GunSoundType type, String sound) {
        return new ConfigNodeBuilder(type.name(), ConfigNodeType.CATEGORY)
                .addChild(new ConfigNodeBuilder("Sound", ConfigNodeType.ENTRY).setValue(sound))
                .addChild(new ConfigNodeBuilder("Volume", ConfigNodeType.ENTRY).setValue(1.0))
                .addChild(new ConfigNodeBuilder("Pitch", ConfigNodeType.ENTRY).setValue(1.0))
                .addChild(new ConfigNodeBuilder("Surrounding distance", ConfigNodeType.ENTRY).setValue(15.0))
                .build();
    }
}
