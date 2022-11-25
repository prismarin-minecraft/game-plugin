package in.prismar.game.map;

import in.prismar.api.PrismarinApi;
import in.prismar.api.map.GameMapLeaderboardEntry;
import in.prismar.api.map.GameMapProvider;
import in.prismar.api.placeholder.PlaceholderStore;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.gun.Gun;
import in.prismar.game.item.gun.type.AmmoType;
import in.prismar.game.map.model.GameMap;
import in.prismar.game.map.model.GameMapPlayer;
import in.prismar.game.map.powerup.PowerUpRegistry;
import in.prismar.game.map.repository.FileGameMapRepository;
import in.prismar.game.map.repository.GameMapRepository;
import in.prismar.game.map.rotation.GameMapRotator;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.tuple.Tuple;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameMapFacade implements GameMapProvider {

    @Inject
    private CustomItemRegistry itemRegistry;

    @Inject
    private GameStatsDistributor statsDistributor;
    
    private GameMapRepository repository;
    private GameMapRotator rotator;

    @Inject
    private PowerUpRegistry powerUpRegistry;

    private final UserProvider<User> userProvider;

    private final PlaceholderStore placeholderStore;


    public GameMapFacade(Game game) {
        this.repository = new FileGameMapRepository(game.getDefaultDirectory());
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.placeholderStore = PrismarinApi.getProvider(PlaceholderStore.class);
        Bukkit.getScheduler().runTaskTimer(game, rotator = new GameMapRotator(this), 5, 5);
    }

    public void sendMessage(String message) {
        if(getRotator().getCurrentMap() != null) {
            sendMessage(getRotator().getCurrentMap(), message);
        }
    }

    public void sendMessage(GameMap map, String message) {
        for(GameMapPlayer player : map.getPlayers().values()) {
            player.getPlayer().sendMessage(message);
        }
    }

    public void updateLeaderboard(GameMap map) {
        List<GameMapLeaderboardEntry> list = new ArrayList<>();
        List<Tuple<Player, Integer>> entries = new ArrayList<>();
        for(GameMapPlayer mapPlayer : map.getPlayers().values()) {
            entries.add(new Tuple<>(mapPlayer.getPlayer(), mapPlayer.getKills()));
        }
        entries.sort((o1, o2) -> Integer.compare(o2.getSecond(), o1.getSecond()));

        for(Tuple<Player, Integer> entry : entries) {
            list.add(new GameMapLeaderboardEntry(entry.getFirst().getUniqueId(), entry.getFirst().getName(), entry.getSecond()));
        }
        map.setLeaderboard(list);
    }

    @Override
    public Optional<String> getMapByPlayer(UUID uuid) {
        for(GameMap map : repository.findAll()) {
            if(map.getPlayers().containsKey(uuid)) {
                return Optional.of(map.getId());
            }
        }
        return Optional.empty();
    }

    @Override
    public List<GameMapLeaderboardEntry> getLeaderboard() {
        if(getRotator().getCurrentMap() != null) {
            return getLeaderboard(getRotator().getCurrentMap().getId());
        }
        return Collections.emptyList();
    }

    @Override
    public List<GameMapLeaderboardEntry> getLeaderboard(String id) {
        GameMap map = getRepository().findById(id);
        if(map != null) {
            if(map.getLeaderboard() != null) {
                return map.getLeaderboard();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String getMapName(String id) {
        if(repository.existsById(id)) {
            return repository.findById(id).getFancyName();
        }
        return "None";
    }

    public Optional<GameMap> getMapByPlayer(Player player) {
        for(GameMap map : repository.findAll()) {
            if(map.getPlayers().containsKey(player.getUniqueId())) {
                return Optional.of(map);
            }
        }
        return Optional.empty();
    }


    public boolean isCurrentlyPlaying(Player player) {
        return isInMap(player.getUniqueId());
    }



    public void fillAmmo(Player player) {
        for (int i = 9; i < 13; i++) {
            ItemStack item = AmmoType.AR.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 13; i < 15; i++) {
            ItemStack item = AmmoType.SNIPER.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 15; i < 17; i++) {
            ItemStack item = AmmoType.SHOTGUN.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 17; i < 19; i++) {
            ItemStack item = AmmoType.SMG.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 19; i < 21; i++) {
            ItemStack item = AmmoType.PISTOL.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
    }

    public void respawn(Player player) {
        player.teleport(rotator.getCurrentMap().getRandomSpawn());
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.65f, 1);
        resetPlayer(player, GameMode.ADVENTURE);

        fillAmmo(player);

        final int[] slots = {0, 1, 2, 3, 4, 5, 6, 7, 27, 28, 29, 30, 31, 32, 33, 34, 35};

        int index = 0;
        for(CustomItem customItem : itemRegistry.getItems().values()) {
            if(customItem instanceof Gun gun) {
                player.getInventory().setItem(slots[index], itemRegistry.createGunItem(gun.getId()));
                index++;
            }
        }

        player.getInventory().setItem(8, itemRegistry.createItem("Grenade"));

    }

    public void join(Player player) {
        rotator.getCurrentMap().getPlayers().put(player.getUniqueId(), new GameMapPlayer(player));
        respawn(player);

        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        provider.recreateTablist(player);
        provider.recreateSidebar(player);
        updateLeaderboard(rotator.getCurrentMap());
    }

    public void leave(Player player) {
        rotator.getCurrentMap().getPlayers().remove(player.getUniqueId());
        resetPlayer(player, GameMode.ADVENTURE);
        statsDistributor.resetKillstreak(player);
        player.performCommand("spawn");
        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        provider.recreateTablist(player);
        provider.recreateSidebar(player);
        updateLeaderboard(rotator.getCurrentMap());
    }

    private void resetPlayer(Player player, GameMode mode) {
        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(mode);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20);
        player.setFoodLevel(20);

        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.removeTag("doubleJump");
    }

    public GameMap getRandomMap() {
        GameMap[] maps = getRepository().findAll().toArray(new GameMap[0]);
        return maps[MathUtil.random(maps.length-1)];
    }

    @Override
    public boolean isInMap(UUID uuid) {
        if(getRotator().getCurrentMap() != null) {
            return getRotator().getCurrentMap().getPlayers().containsKey(uuid);
        }
        return false;
    }

    @Override
    public boolean isInMap(UUID uuid, String name) {
        if(repository.existsById(name)) {
            GameMap map = repository.findById(name);
            return map.getPlayers().containsKey(uuid);
        }
        return false;
    }

    @Override
    public String getCurrentMapNameColored() {
        return rotator.getCurrentMap().getIcon().getItem().getItemMeta().getDisplayName();
    }
}
