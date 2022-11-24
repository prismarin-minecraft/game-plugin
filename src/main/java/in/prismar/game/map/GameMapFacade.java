package in.prismar.game.map;

import in.prismar.api.PrismarinApi;
import in.prismar.api.placeholder.PlaceholderStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.gun.type.AmmoType;
import in.prismar.game.map.model.GameMap;
import in.prismar.game.map.powerup.PowerUpRegistry;
import in.prismar.game.map.repository.FileGameMapRepository;
import in.prismar.game.map.repository.GameMapRepository;
import in.prismar.game.map.rotation.GameMapRotator;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameMapFacade {

    @Inject
    private CustomItemRegistry itemRegistry;
    
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
            for(Player player : getRotator().getCurrentMap().getPlayers().values()) {
                player.sendMessage(message);
            }
        }

    }

    public boolean isCurrentlyPlaying(Player player) {
        if(getRotator().getCurrentMap() != null) {
            return getRotator().getCurrentMap().getPlayers().containsKey(player.getUniqueId());
        }
        return false;
    }



    public void fillAmmo(Player player) {
        for (int i = 9; i < 15; i++) {
            ItemStack item = AmmoType.AR.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 15; i < 17; i++) {
            ItemStack item = AmmoType.SNIPER.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 17; i < 20; i++) {
            ItemStack item = AmmoType.SHOTGUN.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
    }

    public void respawn(Player player) {
        player.teleport(rotator.getCurrentMap().getRandomSpawn());
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.65f, 1);
        resetPlayer(player, GameMode.ADVENTURE);
        player.getInventory().addItem(itemRegistry.createGunItem("G36"));
        player.getInventory().addItem(itemRegistry.createGunItem("L96"));
        player.getInventory().addItem(itemRegistry.createGunItem("Spas-12"));
        fillAmmo(player);


    }

    public void join(Player player) {
        rotator.getCurrentMap().getPlayers().put(player.getUniqueId(), player);
        respawn(player);

        placeholderStore.setPlayerPlaceholder(player.getUniqueId(), "playing", true);
    }

    public void leave(Player player) {
        rotator.getCurrentMap().getPlayers().remove(player.getUniqueId());
        resetPlayer(player, GameMode.ADVENTURE);
        player.performCommand("spawn");
        placeholderStore.setPlayerPlaceholder(player.getUniqueId(), "playing", false);
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
}
