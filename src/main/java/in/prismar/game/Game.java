package in.prismar.game;

import dev.sergiferry.playernpc.api.NPCLib;
import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.map.ExtractionProvider;
import in.prismar.api.map.GameMapProvider;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.web.WebServer;
import in.prismar.game.web.impl.ItemsRoute;
import in.prismar.library.meta.MetaRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.entity.GlowingEntities;
import in.prismar.library.spigot.meta.SpigotCommandProcessor;
import in.prismar.library.spigot.meta.SpigotListenerProcessor;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import in.prismar.library.spigot.meta.anno.AutoListener;
import in.prismar.library.spigot.setup.SpigotSetup;
import lombok.Getter;
import org.bukkit.Particle;
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

    private GlowingEntities glowingEntities;


    @Inject
    private GameMapFacade mapFacade;

    @Inject
    private CustomItemRegistry itemRegistry;

    @Inject
    private ExtractionFacade extractionFacade;

    @Inject
    private AirDropRegistry airDropRegistry;

    private RegionProvider regionProvider;
    private WebServer webServer;



    @Override
    public void onEnable() {
        initialize();
    }

    @Override
    public void onDisable() {
        airDropRegistry.despawnAll();
    }

    private void initialize() {
        this.glowingEntities = new GlowingEntities(this);
        this.setup = new SpigotSetup(this, "game");
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);

        this.metaRegistry = new MetaRegistry();
        this.metaRegistry.registerProcessor(AutoCommand.class, new SpigotCommandProcessor(setup, metaRegistry));
        this.metaRegistry.registerProcessor(AutoListener.class, new SpigotListenerProcessor(setup, metaRegistry));

        this.metaRegistry.registerEntity(this);
        this.metaRegistry.build(this.getClassLoader(), "in.prismar.game");

        setup.register();

        initApi();

        NPCLib.getInstance().registerPlugin(this);

        initializeWebServer();
    }

    private void initApi() {
        PrismarinApi.registerProvider(GameMapProvider.class, mapFacade);
        PrismarinApi.registerProvider(ExtractionProvider.class, extractionFacade);
    }

    private void initializeWebServer() {
        ConfigStore store = PrismarinApi.getProvider(ConfigStore.class);
        final int port = Integer.valueOf(store.getProperty("live.web.port"));
        this.webServer = new WebServer(store.getProperty("live.web.base.path"), port);
        this.webServer.addRoute(new ItemsRoute(itemRegistry));
        this.webServer.initializePaths();
        System.out.println("Live web server started on port: " + port);
    }

    public String getDefaultDirectory() {
        return new StringBuilder("plugins").append(File.separator).append(getDescription().getName()).append(File.separator).toString();
    }

}
