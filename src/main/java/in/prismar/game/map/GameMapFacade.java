package in.prismar.game.map;

import in.prismar.api.PrismarinApi;
import in.prismar.api.map.GameMapLeaderboardEntry;
import in.prismar.api.map.GameMapProvider;
import in.prismar.api.placeholder.PlaceholderStore;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.ArsenalItem;
import in.prismar.game.Game;
import in.prismar.game.arsenal.ArsenalService;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.type.AmmoType;
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
import in.prismar.library.spigot.item.PersistentItemDataUtil;
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

    private GameMapRepository repository;
    private GameMapRotator rotator;


    private final Game game;

    public GameMapFacade(Game game) {
        this.game = game;
        this.repository = new FileGameMapRepository(game.getDefaultDirectory());
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.placeholderStore = PrismarinApi.getProvider(PlaceholderStore.class);
        Bukkit.getScheduler().runTaskTimer(game, rotator = new GameMapRotator(this), 5, 5);
    }

    public void sendMessage(String message) {
        if (getRotator().getCurrentMap() != null) {
            sendMessage(getRotator().getCurrentMap(), message);
        }
    }

    public void sendMessage(GameMap map, String message) {
        for (GameMapPlayer player : map.getPlayers().values()) {
            player.getPlayer().sendMessage(message);
        }
    }

    public void updateLeaderboard(GameMap map) {
        List<GameMapLeaderboardEntry> list = new ArrayList<>();
        List<Tuple<Player, Integer>> entries = new ArrayList<>();
        for (GameMapPlayer mapPlayer : map.getPlayers().values()) {
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
        for (GameMap map : repository.findAll()) {
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
        GameMap map = getRepository().findById(id);
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

    public Optional<GameMap> getMapByPlayer(Player player) {
        for (GameMap map : repository.findAll()) {
            if (map.getPlayers().containsKey(player.getUniqueId())) {
                return Optional.of(map);
            }
        }
        return Optional.empty();
    }


    public boolean isCurrentlyPlaying(Player player) {
        return isInMap(player.getUniqueId());
    }


    public void fillAmmo(Player player) {
        for (int i = 9; i < 12; i++) {
            ItemStack item = AmmoType.AR.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 12; i < 13; i++) {
            ItemStack item = AmmoType.SNIPER.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 13; i < 14; i++) {
            ItemStack item = AmmoType.SHOTGUN.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 14; i < 17; i++) {
            ItemStack item = AmmoType.SMG.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 17; i < 18; i++) {
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

        User user = arsenalService.manage(player);
        ItemStack primary = createArsenalItem(user, "primary");
        if (primary != null) {
            player.getInventory().setItem(0, primary);
        }
        ItemStack secondary = createArsenalItem(user, "secondary");
        if (secondary != null) {
            player.getInventory().setItem(1, secondary);
        }

        player.getInventory().setItem(3, itemRegistry.createItem("Grenade"));
    }

    private ItemStack createArsenalItem(User user, String key) {
        ArsenalItem item = arsenalService.getItem(user, key);
        if (item != null) {
            ItemStack stack = item.getItem().clone();
            CustomItem customItem = itemRegistry.getItemByStack(stack);
            if (customItem != null) {
                if(customItem instanceof Gun gun) {
                    int maxAmmo = gun.getMaxAmmo();
                    for(Attachment attachment : gun.getAttachments(game, stack)) {
                        maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
                    }
                    PersistentItemDataUtil.setInteger(game, stack, Gun.AMMO_KEY, maxAmmo);
                }
            }
            return stack;
        }
        return null;
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
