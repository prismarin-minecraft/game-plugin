package in.prismar.game;

import in.prismar.api.PrismarinApi;
import in.prismar.api.bounty.BountyProvider;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.configuration.node.ConfigNodeProvider;
import in.prismar.api.core.CoreProvider;
import in.prismar.api.game.GameProvider;
import in.prismar.api.game.ffa.FFAMapProvider;
import in.prismar.api.game.hardpoint.HardpointProvider;
import in.prismar.api.item.CustomItemProvider;
import in.prismar.api.item.TeaProvider;
import in.prismar.api.meta.Provider;
import in.prismar.api.meta.ProviderProcessor;
import in.prismar.api.party.PartyProvider;
import in.prismar.api.region.RegionProvider;
import in.prismar.api.warzone.WarzoneProvider;
import in.prismar.api.warzone.dungeon.DungeonProvider;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.animation.AnimationFacade;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.bounty.BountyService;
import in.prismar.game.database.RedisContext;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.item.CustomItemAmmoProvider;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.CustomItemTeaService;
import in.prismar.game.ai.AIRegistry;
import in.prismar.game.party.PartyRegistry;
import in.prismar.game.perk.PerkService;
import in.prismar.game.storage.StorageService;
import in.prismar.game.tracer.BulletTracerRegistry;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.game.warzone.dungeon.DungeonService;
import in.prismar.game.web.WebServer;
import in.prismar.game.web.config.file.ConfigNodeFile;
import in.prismar.game.web.impl.PlayerRoute;
import in.prismar.game.web.impl.ReportsRoute;
import in.prismar.game.web.impl.VoteRoute;
import in.prismar.game.web.impl.banner.BannerRoute;
import in.prismar.game.web.impl.config.GetConfigRoute;
import in.prismar.game.web.impl.config.GetConfigTemplateRoute;
import in.prismar.game.web.impl.config.PostConfigRoute;
import in.prismar.game.web.impl.items.ItemsRoute;
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
    private FFAFacade mapFacade;

    @Inject
    private AIRegistry aiRegistry;

    @Inject
    private CustomItemTeaService teaService;

    @Inject
    private AnimationFacade animationFacade;

    @Inject
    private StorageService storageService;

    @Inject
    private PerkService perkService;

    @Inject
    private BattleRoyaleService battleRoyaleService;

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
    private AirDropRegistry airDropRegistry;

    @Inject
    private WarzoneService warzoneService;

    @Inject
    private ConfigNodeFile configNodeFile;

    @Inject
    private DungeonService dungeonService;

    @Inject
    private BountyService bountyService;


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
        this.metaRegistry.registerProcessor(Provider.class, new ProviderProcessor(metaRegistry));

        this.metaRegistry.registerEntity(this);
        this.metaRegistry.build(this.getClassLoader(), "in.prismar.game");

        setup.register();

        initApi();

        initializeWebServer();
    }

    private void initApi() {
        PrismarinApi.registerProvider(FFAMapProvider.class, mapFacade);
        PrismarinApi.registerProvider(HardpointProvider.class, hardpointFacade);
        PrismarinApi.registerProvider(GameProvider.class, this);
        PrismarinApi.registerProvider(PartyProvider.class, partyRegistry);
        PrismarinApi.registerProvider(WarzoneProvider.class, warzoneService);
        PrismarinApi.registerProvider(ConfigNodeProvider.class, configNodeFile);
        PrismarinApi.registerProvider(DungeonProvider.class, dungeonService);
        PrismarinApi.registerProvider(TeaProvider.class, teaService);
        PrismarinApi.registerProvider(CustomItemProvider.class, itemRegistry);
        PrismarinApi.registerProvider(BountyProvider.class, bountyService);
    }

    private void initializeWebServer() {
        try {
            ConfigStore store = PrismarinApi.getProvider(ConfigStore.class);
            CoreProvider coreProvider = PrismarinApi.getProvider(CoreProvider.class);
            final int port = Integer.valueOf(store.getProperty("live.web.port"));
            this.webServer = new WebServer(store.getProperty("live.web.base.path"), port, coreProvider.isDevMode());
            this.webServer.addRoute(new ItemsRoute(itemRegistry));
            this.webServer.addRoute(new PlayerRoute(mapFacade));
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
        return mapFacade.isCurrentlyPlaying(player) || hardpointFacade.isCurrentlyPlaying(player);
    }


    public String getDefaultDirectory() {
        return "plugins" + File.separator + getDescription().getName() + File.separator;
    }

}
