package in.prismar.game.web.config.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.model.Skin;
import in.prismar.game.item.reader.impl.GunData;
import in.prismar.game.item.reader.impl.GunSoundData;
import in.prismar.game.web.config.builder.ConfigNodeBuilder;
import in.prismar.game.web.config.file.ConfigNodeFile;
import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.config.model.ConfigNodeType;
import in.prismar.library.common.EnumUtil;
import in.prismar.library.file.toml.TomlConfig;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class MigrateSubCommand extends HelpSubCommand<CommandSender> {

    private final ConfigNodeFile file;

    public MigrateSubCommand(ConfigNodeFile file) {
        super("migrate");
        this.file = file;

        setDescription("Migrate all files to new config nodes");
    }

    @Override
    public boolean send(CommandSender sender, SpigotArguments arguments) throws CommandException {
        sender.sendMessage(PrismarinConstants.PREFIX + "§7Start migrating toml files to new config nodes...");
        CompletableFuture.runAsync(() -> {
            File directory = new File(file.getGame().getDefaultDirectory() + "items" + File.separator + "guns" + File.separator);
            for(File rawFile : directory.listFiles()) {
                TomlConfig<GunData> tomlConfig = new TomlConfig<>(rawFile.getAbsolutePath(), GunData.class);
                if(tomlConfig.getEntity() != null) {
                    GunData data = tomlConfig.getEntity();
                    final String categoryId = "guns." + data.getType().getDisplayName().toLowerCase().replace(" ", "_");
                    System.out.println(categoryId);
                    ConfigNode parent = file.getNode(categoryId);
                    if(parent == null) {
                        continue;
                    }

                    final String name = ChatColor.stripColor(data.getDisplayName()).replace(" ", "_");
                    ConfigNodeBuilder builder = new ConfigNodeBuilder(name, ConfigNodeType.CATEGORY);

                    builder.addChild(new ConfigNodeBuilder("ID", ConfigNodeType.ENTRY).setValue(data.getId()));
                    builder.addChild(new ConfigNodeBuilder("Material", ConfigNodeType.ENTRY).setValue(data.getMaterial().name()));
                    builder.addChild(new ConfigNodeBuilder("Display name", ConfigNodeType.ENTRY).setValue(data.getDisplayName().replace("§", "&")));
                    builder.addChild(new ConfigNodeBuilder("Custom model data", ConfigNodeType.ENTRY).setValue(data.getCustomModelData()));
                    builder.addChild(new ConfigNodeBuilder("Ammo type", ConfigNodeType.ENTRY).setValue(data.getAmmoType().name()).addOptions(EnumUtil.getEnumNames(AmmoType.values())));
                    builder.addChild(new ConfigNodeBuilder("Shoot particle", ConfigNodeType.ENTRY).setValue(data.getShootParticle().name()));
                    builder.addChild(new ConfigNodeBuilder("Range", ConfigNodeType.ENTRY).setValue(data.getRange()));
                    builder.addChild(new ConfigNodeBuilder("Fire rate", ConfigNodeType.ENTRY).setValue(data.getFireRate()));
                    builder.addChild(new ConfigNodeBuilder("Spread", ConfigNodeType.ENTRY).setValue(data.getSpread()));
                    builder.addChild(new ConfigNodeBuilder("Sneak spread", ConfigNodeType.ENTRY).setValue(data.getSneakSpread()));
                    builder.addChild(new ConfigNodeBuilder("Bullets per shot", ConfigNodeType.ENTRY).setValue(data.getBulletsPerShot()));
                    builder.addChild(new ConfigNodeBuilder("Max ammo", ConfigNodeType.ENTRY).setValue(data.getMaxAmmo()));
                    builder.addChild(new ConfigNodeBuilder("Reload time in ticks", ConfigNodeType.ENTRY).setValue(data.getReloadTimeInTicks()));
                    builder.addChild(new ConfigNodeBuilder("Head damage", ConfigNodeType.ENTRY).setValue(data.getHeadDamage()));
                    builder.addChild(new ConfigNodeBuilder("Body damage", ConfigNodeType.ENTRY).setValue(data.getBodyDamage()));
                    builder.addChild(new ConfigNodeBuilder("Leg damage", ConfigNodeType.ENTRY).setValue(data.getLegDamage()));
                    builder.addChild(new ConfigNodeBuilder("Zoom", ConfigNodeType.ENTRY).setValue(data.getZoom()));
                    builder.addChild(new ConfigNodeBuilder("Preview image", ConfigNodeType.ENTRY).setValue(data.getPreviewImage()));

                    final String skinsTemplateId = "items_skin";

                    ConfigNodeBuilder skins = new ConfigNodeBuilder("Skins", ConfigNodeType.CATEGORY);
                    skins.setTemplate(skinsTemplateId);
                    builder.addChild(skins);

                    for(Skin skin : data.getSkins()) {
                        final String skinName = ChatColor.stripColor(skin.getDisplayName()).replace(" ", "_");
                        ConfigNodeBuilder skinCategory = new ConfigNodeBuilder(skinName, ConfigNodeType.CATEGORY);
                        skinCategory.addChild(new ConfigNodeBuilder("Display name", ConfigNodeType.ENTRY).setValue(skin.getDisplayName().replace("§", "&")));
                        skinCategory.addChild(new ConfigNodeBuilder("Preview image", ConfigNodeType.ENTRY).setValue(skin.getPreviewImage()));
                        skinCategory.addChild(new ConfigNodeBuilder("Custom model data", ConfigNodeType.ENTRY).setValue(skin.getData()));

                        skins.getChildren().put(skinName, skinCategory.build());
                    }

                    final String soundsTemplateId = "items_sounds";

                    ConfigNodeBuilder sounds = new ConfigNodeBuilder("Sounds", ConfigNodeType.CATEGORY);
                    sounds.setTemplate(soundsTemplateId);
                    builder.addChild(sounds);

                    parent.getChildren().put(name, builder.build());
                    file.save();
                }
            }
            file.refreshNodeIds();
            sender.sendMessage(PrismarinConstants.PREFIX + "§aFinished migrating.");
        }).exceptionally(new Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        });
        return true;
    }
}
