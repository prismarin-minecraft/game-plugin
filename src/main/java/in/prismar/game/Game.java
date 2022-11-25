package in.prismar.game;

import in.prismar.api.PrismarinApi;
import in.prismar.api.map.GameMapProvider;
import in.prismar.game.map.GameMapFacade;
import in.prismar.game.map.model.GameMap;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.meta.SpigotCommandProcessor;
import in.prismar.library.spigot.meta.SpigotListenerProcessor;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.meta.anno.AutoListener;
import in.prismar.library.spigot.setup.SpigotSetup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service(autoRegister = false)
public class Game extends JavaPlugin {

    private SpigotSetup setup;

    private MetaRegistry metaRegistry;


    @Inject
    private GameMapFacade mapFacade;


    @Override
    public void onEnable() {
        initialize();
    }

    private void initialize() {
        this.setup = new SpigotSetup(this, "game");
        this.metaRegistry = new MetaRegistry();
        this.metaRegistry.registerProcessor(AutoCommand.class, new SpigotCommandProcessor(setup, metaRegistry));
        this.metaRegistry.registerProcessor(AutoListener.class, new SpigotListenerProcessor(setup, metaRegistry));

        this.metaRegistry.registerEntity(this);
        this.metaRegistry.build(this.getClassLoader(), "in.prismar.game");

        setup.register();

        initApi();
    }

    private void initApi() {
        PrismarinApi.registerProvider(GameMapProvider.class, mapFacade);
    }

    public String getDefaultDirectory() {
        return new StringBuilder("plugins").append(File.separator).append(getDescription().getName()).append(File.separator).toString();
    }

}
