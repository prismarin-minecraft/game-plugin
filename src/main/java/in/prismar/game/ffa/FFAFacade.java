package in.prismar.game.ffa;

import in.prismar.api.PrismarinApi;
import in.prismar.api.map.GameMapLeaderboardEntry;
import in.prismar.api.map.GameMapProvider;
import in.prismar.api.placeholder.PlaceholderStore;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.arsenal.ArsenalService;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.ffa.model.FFAMap;
import in.prismar.game.ffa.model.FFAMapPlayer;
import in.prismar.game.ffa.powerup.PowerUpRegistry;
import in.prismar.game.ffa.repository.FileFFAMapRepository;
import in.prismar.game.ffa.repository.FFAMapRepository;
import in.prismar.game.ffa.rotation.FFAMapRotator;
import in.prismar.game.stats.GameStatsDistributor;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.common.tuple.Tuple;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
public class FFAFacade implements GameMapProvider {

    private final UserProvider<User> userProvider;

    private final PlaceholderStore placeholderStore;

    @Inject
    private CustomItemRegistry itemRegistry;

    @Inject
    private ArsenalService arsenalService;

    @Inject
    private GameStatsDistributor statsDistributor;

    @Inject
    private PowerUpRegistry powerUpRegistry;

    private final FFAMapRepository repository;
    private final FFAMapRotator rotator;


    private final Game game;

    @Setter
    private boolean open = true;

    public FFAFacade(Game game) {
        this.game = game;
        this.repository = new FileFFAMapRepository(game.getDefaultDirectory());
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.placeholderStore = PrismarinApi.getProvider(PlaceholderStore.class);
        Bukkit.getScheduler().runTaskTimer(game, rotator = new FFAMapRotator(this), 1, 1);
    }

    public void close() {
        Iterator<FFAMapPlayer> iterator = new ArrayList<>(getRotator().getCurrentMap().getPlayers().values()).iterator();
        while (iterator.hasNext()) {
            FFAMapPlayer player = iterator.next();
            leave(player.getPlayer());
        }
        this.open = false;
    }

    public void sendMessage(String message) {
        if (getRotator().getCurrentMap() != null) {
            sendMessage(getRotator().getCurrentMap(), message);
        }
    }

    public void sendMessage(FFAMap map, String message) {
        for (FFAMapPlayer player : map.getPlayers().values()) {
            player.getPlayer().sendMessage(message);
        }
    }

    public void updateLeaderboard(FFAMap map) {
        List<GameMapLeaderboardEntry> list = new ArrayList<>();
        List<Tuple<Player, Integer>> entries = new ArrayList<>();
        for (FFAMapPlayer mapPlayer : map.getPlayers().values()) {
            entries.add(new Tuple<>(mapPlayer.getPlayer(), mapPlayer.getKills()));
        }
        entries.sort((o1, o2) -> Integer.compare(o2.getSecond(), o1.getSecond()));

        for (Tuple<Player, Integer> entry : entries) {
            list.add(new GameMapLeaderboardEntry(entry.getFirst().getUniqueId(), entry.getFirst().getName(), entry.getSecond()));
        }
        map.setLeaderboard(list);
    }

    @Override
    public long getNextMapRotationTimestamp() {
        return rotator.getNextRotate();
    }

    @Override
    public long getVoteMapRotationTimestamp() {
        return rotator.getNextRotateVote();
    }

    @Override
    public Optional<String> getMapByPlayer(UUID uuid) {
        for (FFAMap map : repository.findAll()) {
            if (map.getPlayers().containsKey(uuid)) {
                return Optional.of(map.getId());
            }
        }
        return Optional.empty();
    }

    @Override
    public List<GameMapLeaderboardEntry> getLeaderboard() {
        if (getRotator().getCurrentMap() != null) {
            return getLeaderboard(getRotator().getCurrentMap().getId());
        }
        return Collections.emptyList();
    }

    @Override
    public List<GameMapLeaderboardEntry> getLeaderboard(String id) {
        FFAMap map = getRepository().findById(id);
        if (map != null) {
            if (map.getLeaderboard() != null) {
                return map.getLeaderboard();
            }
        }
        return Collections.emptyList();
    }

    @Override
    public String getMapName(String id) {
        if (repository.existsById(id)) {
            return repository.findById(id).getFancyName();
        }
        return "None";
    }

    public Optional<FFAMap> getMapByPlayer(Player player) {
        for (FFAMap map : repository.findAll()) {
            if (map.getPlayers().containsKey(player.getUniqueId())) {
                return Optional.of(map);
            }
        }
        return Optional.empty();
    }


    public boolean isCurrentlyPlaying(Player player) {
        return isInMap(player.getUniqueId());
    }




    public void respawn(Player player) {
        player.teleport(rotator.getCurrentMap().getRandomSpawn());
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.65f, 1);
        resetPlayer(player);

        arsenalService.fillAmmo(player);

        arsenalService.giveLoadout(player);

        player.getInventory().setItem(8, itemRegistry.createItem("FFALeave"));
    }




    public void join(Player player) {
        rotator.getCurrentMap().getPlayers().put(player.getUniqueId(), new FFAMapPlayer(player));
        respawn(player);

        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        provider.recreateTablist(player);
        provider.recreateSidebar(player);
        updateLeaderboard(rotator.getCurrentMap());
    }

    public void leave(Player player) {
        FFAMapPlayer mapPlayer = rotator.getCurrentMap().getPlayers().remove(player.getUniqueId());
        resetPlayer(player);
        statsDistributor.resetKillstreak(player);
        player.performCommand("spawn");
        ScoreboardProvider provider = PrismarinApi.getProvider(ScoreboardProvider.class);
        provider.recreateTablist(player);
        provider.recreateSidebar(player);
        updateLeaderboard(rotator.getCurrentMap());

        player.getInventory().setArmorContents(mapPlayer.getArmor());
        player.getInventory().setStorageContents(mapPlayer.getContent());
    }

    private void resetPlayer(Player player) {
        player.getInventory().clear();
        player.setAllowFlight(false);
        player.setFlying(false);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
        player.setHealth(20);
        player.setFoodLevel(20);

        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.removeTag("doubleJump");
    }

    public FFAMap getRandomMap() {
        FFAMap[] maps = getRepository().findAll().toArray(new FFAMap[0]);
        return maps[MathUtil.random(maps.length - 1)];
    }

    @Override
    public boolean isInMap(UUID uuid) {
        if (getRotator().getCurrentMap() != null) {
            return getRotator().getCurrentMap().getPlayers().containsKey(uuid);
        }
        return false;
    }

    @Override
    public boolean isInMap(UUID uuid, String name) {
        if (repository.existsById(name)) {
            FFAMap map = repository.findById(name);
            return map.getPlayers().containsKey(uuid);
        }
        return false;
    }

    @Override
    public String getCurrentMapNameColored() {
        return rotator.getCurrentMap().getIcon().getItem().getItemMeta().getDisplayName();
    }
}
