package in.prismar.game.listener;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.Game;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HexFormat;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class PlayerJoinListener implements Listener {

    @Inject
    private Game game;

    private final ConfigStore store;

    private final String url;
    private final byte[] hash;

    public PlayerJoinListener() {
        this.store = PrismarinApi.getProvider(ConfigStore.class);
        this.url = store.getProperty("resourcepack.url");
        this.hash = HexFormat.of().parseHex(store.getProperty("resourcepack.hash"));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        Bukkit.getScheduler().runTaskLater(game, () -> {
            //player.setResourcePack(url, hash, false);
        }, 20);
    }

}
