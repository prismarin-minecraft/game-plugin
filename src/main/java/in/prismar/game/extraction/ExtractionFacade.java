package in.prismar.game.extraction;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.compass.CompassEntryReachEvent;
import in.prismar.api.compass.CompassProvider;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.map.ExtractionProvider;
import in.prismar.api.region.RegionProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.warp.WarpProvider;
import in.prismar.game.Game;
import in.prismar.game.airdrop.AirDrop;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.airdrop.event.AirdropCallEvent;
import in.prismar.game.airdrop.event.AirdropRemoveEvent;
import in.prismar.game.extraction.corpse.Corpse;
import in.prismar.game.extraction.map.ExtractionMapFile;
import in.prismar.game.extraction.task.ExtractionChecker;
import in.prismar.game.extraction.task.ExtractionCorpseDespawner;
import in.prismar.library.common.random.UniqueRandomizer;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.text.CenteredMessage;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class ExtractionFacade implements ExtractionProvider {


    @Inject
    private AirDropRegistry airDropRegistry;

    private final ExtractionMapFile mapFile;
    private final Game game;
    private final RegionProvider regionProvider;

    private ExtractionChecker checker;

    private final List<Corpse> corpses;

    private final ConfigStore configStore;

    private final UserProvider<User> userProvider;

    private final CompassProvider compassProvider;

    @Setter
    private boolean running;
    private final int currentMapSpawnIndex = 0;

    public ExtractionFacade(Game game) {
        this.game = game;
        this.mapFile = new ExtractionMapFile(game.getDefaultDirectory());
        this.corpses = new ArrayList<>();
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.compassProvider = PrismarinApi.getProvider(CompassProvider.class);
       // Bukkit.getScheduler().runTaskTimerAsynchronously(game, checker = new ExtractionChecker(this), 20, 20);
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new ExtractionCorpseDespawner(this), 20, 20);

        compassProvider.getEventBus().subscribe(CompassEntryReachEvent.class, entity -> {
            if(entity.getName().equalsIgnoreCase("Last Death")) {
                compassProvider.removeEntry(entity.getPlayer(), entity.getName());
                User user = userProvider.getUserByUUID(entity.getPlayer().getUniqueId());
                user.removeTag("lastDeath");
            }
        });
    }

    @SafeInitialize
    private void initialize() {
        airDropRegistry.getEventBus().subscribe(AirdropCallEvent.class, entity -> {
            World world = getExtractionWorld();
            if(world != null) {
                for(Player player : world.getPlayers()) {
                    updateCompass(player);
                }
            }
        });
        airDropRegistry.getEventBus().subscribe(AirdropRemoveEvent.class, entity -> {
            World world = getExtractionWorld();
            if(world != null) {
                for(Player player : world.getPlayers()) {
                    updateCompass(player);
                }
            }
        });
    }


    public void open() {
        setRunning(true);
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage(PrismarinConstants.BORDER));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage("§c§lEXTRACTION §7is now open!"));
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage("§7You can join with §8/§cextraction join"));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage(PrismarinConstants.BORDER));
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 0.7f, 1);
        }
        changeNPCText("§aEnabled");

    }

    private void changeNPCText(String text) {
        /*Plugin plugin = Bukkit.getPluginManager().getPlugin("PlayerNPC");
        if(plugin != null) {
            NPC.Global npc = NPCLib.getInstance().getGlobalNPC(plugin, "extraction");
            npc.setText("§4§lEXTRACTION", text);
            npc.forceUpdate();
        }*/

    }

    public void close() {
        setRunning(false);
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage(PrismarinConstants.BORDER));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage("§c§lEXTRACTION §7is now closed!"));
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(CenteredMessage.createCentredMessage(PrismarinConstants.BORDER));
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT, 0.7f, 1);
        }
        World world = getExtractionWorld();
        if(world != null) {
            WarpProvider provider = PrismarinApi.getProvider(WarpProvider.class);
            for(Player player : world.getPlayers()) {
                provider.teleportToSpawn(player);
                compassProvider.removeAllEntries(player);
            }
        }
        changeNPCText("§cDisabled");
    }

    public void join(Player player) {
        Location random = getMapSpawnLocation();
        player.teleport(random);

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.6f, 1);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 0.5f, 1);

        for(Corpse corpse : corpses) {
            corpse.getNpc().forceUpdate(player);
        }

        updateCompass(player);
    }

    public List<AirDrop> getSpawnedAirdrops() {
        return airDropRegistry.getAirDrops().stream().filter(airDrop -> airDrop.getLocation().getWorld().getName().equals(getExtractionWorld().getName())).toList();
    }

    public void leave(Player player) {
        WarpProvider provider = PrismarinApi.getProvider(WarpProvider.class);
        provider.teleportToSpawn(player);
        player.setHealth(20);

        compassProvider.removeAllEntries(player);
    }

    public void updateCompass(Player player) {
        compassProvider.removeAllEntries(player);
        for(AirDrop airDrop : getSpawnedAirdrops()) {
            compassProvider.addEntry(player, airDrop.getArmorStand().getLocation(), "Airdrop", "YELLOW");
        }
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(user.containsTag("lastDeath")) {
            compassProvider.addEntry(player, user.getTag("lastDeath"), "Last Death", "RED");
        }
    }

    public void kill(Player player) {
        //TODO: Spawn dead body
        if(!player.getInventory().isEmpty()) {
            long seconds = Long.valueOf(configStore.getProperty("extraction.corpse.despawn"));
            this.corpses.add(new Corpse(game, player, System.currentTimeMillis() + 1000 * seconds));
        }
        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.setTag("lastDeath", player.getLocation());
        player.getInventory().clear();
        leave(player);
    }

    public void sendMessage(String message) {
        World world = getExtractionWorld();
        if(world != null) {
            for(Player player : world.getPlayers()) {
                player.sendMessage(message);
            }
        }
    }

    public Location getMapSpawnLocation() {
        return UniqueRandomizer.getRandom("spawnExtraction", mapFile.getEntity().getSpawns());
    }

    @Override
    public boolean isIn(Player player) {
       if(!getMapFile().getEntity().getSpawns().isEmpty()) {
           final Location location = getMapFile().getEntity().getSpawns().get(0);
           return player.getWorld().getName().equals(location.getWorld().getName());
       }
       return false;
    }

    public World getExtractionWorld() {
        if(getMapFile().getEntity().getSpawns().isEmpty()) {
           return null;
        }
        final Location location = getMapFile().getEntity().getSpawns().get(0);
        return location.getWorld();
    }

    @Override
    public boolean isInSafeZone(Player player) {
        return regionProvider.isInRegionWithFlag(player.getLocation(), "safezone");
    }
}
