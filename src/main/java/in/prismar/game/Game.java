package in.prismar.game;

import dev.sergiferry.playernpc.api.NPCLib;
import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.configuration.node.ConfigNodeProvider;
import in.prismar.api.game.GameProvider;
import in.prismar.api.hardpoint.HardpointProvider;
import in.prismar.api.map.ExtractionProvider;
import in.prismar.api.map.GameMapProvider;
import in.prismar.api.party.PartyProvider;
import in.prismar.api.region.RegionProvider;
import in.prismar.api.warzone.WarzoneProvider;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.database.RedisContext;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.item.CustomItemAmmoProvider;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.party.PartyRegistry;
import in.prismar.game.perk.PerkService;
import in.prismar.game.tracer.BulletTracerRegistry;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.game.web.WebServer;
import in.prismar.game.web.config.file.ConfigNodeFile;
import in.prismar.game.web.impl.ItemsRoute;
import in.prismar.game.web.impl.PlayerRoute;
import in.prismar.game.web.impl.ReportsRoute;
import in.prismar.game.web.impl.VoteRoute;
import in.prismar.game.web.impl.banner.BannerRoute;
import in.prismar.game.web.impl.config.GetConfigRoute;
import in.prismar.game.web.impl.config.GetConfigTemplateRoute;
import in.prismar.game.web.impl.config.PostConfigRoute;
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
import org.bukkit.entity.Player;
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
public class Game extends JavaPlugin implements GameProvider {

    private SpigotSetup setup;

    private MetaRegistry metaRegistry;

    private GlowingEntities glowingEntities;


    @Inject
    private GameMapFacade mapFacade;

    @Inject
    private PerkService perkService;

    @Inject
    private BulletTracerRegistry tracerRegistry;

    @Inject
    private PartyRegistry partyRegistry;

    @Inject
    private RedisContext redisContext;

    @Inject
    private CustomItemAmmoProvider itemAmmoProvider;

    @Inject
    private HardpointFacade hardpointFacade;

    @Inject
    private CustomItemRegistry itemRegistry;

    @Inject
    private ExtractionFacade extractionFacade;

    @Inject
    private AirDropRegistry airDropRegistry;

    @Inject
    private WarzoneService warzoneService;

    @Inject
    private ConfigNodeFile configNodeFile;


    private RegionProvider regionProvider;
    private WebServer webServer;



    @Override
    public void onEnable() {
        initialize();
    }

    @Override
    public void onDisable() {
        mapFacade.close();
        hardpointFacade.close();
        airDropRegistry.despawnAll();
        redisContext.disconnect();
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
        PrismarinApi.registerProvider(HardpointProvider.class, hardpointFacade);
        PrismarinApi.registerProvider(GameProvider.class, this);
        PrismarinApi.registerProvider(PartyProvider.class, partyRegistry);
        PrismarinApi.registerProvider(WarzoneProvider.class, warzoneService);
        PrismarinApi.registerProvider(ConfigNodeProvider.class, configNodeFile);
    }

    private void initializeWebServer() {
        try {
            ConfigStore store = PrismarinApi.getProvider(ConfigStore.class);
            final int port = Integer.valueOf(store.getProperty("live.web.port"));
            this.webServer = new WebServer(store.getProperty("live.web.base.path"), port);
            this.webServer.addRoute(new ItemsRoute(itemRegistry));
            this.webServer.addRoute(new PlayerRoute(mapFacade, extractionFacade));
            this.webServer.addRoute(new VoteRoute());
            this.webServer.addRoute(new BannerRoute(this, webServer));
            this.webServer.addRoute(new GetConfigRoute(this));
            this.webServer.addRoute(new GetConfigTemplateRoute(this));
            this.webServer.addRoute(new PostConfigRoute(this));
            this.webServer.addRoute(new ReportsRoute());
            this.webServer.initializePaths();
        }catch (Exception exception) {
            System.out.println("Cannot start web server: " + exception.getMessage());
        }

    }

    @Override
    public boolean isCurrentlyInGame(Player player) {
        return isCurrentlyPlayingAnyMode(player) || extractionFacade.isIn(player);
    }

    @Override
    public boolean isCurrentlyPlayingAnyMode(Player player) {
        return mapFacade.isCurrentlyPlaying(player) || hardpointFacade.isCurrentlyPlaying(player);
    }


    public String getDefaultDirectory() {
        return new StringBuilder("plugins").append(File.separator).append(getDescription().getName()).append(File.separator).toString();
    }

}
