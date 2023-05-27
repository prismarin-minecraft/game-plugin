package in.prismar.game.warzone.task;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.common.random.UniqueRandomizer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AirdropTask implements Runnable {

    private final WarzoneService service;
    private final ConfigStore configStore;

    private long nextAirdropSpawn;

    public AirdropTask(WarzoneService service) {
        this.service = service;
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);

        resetNextAirdropSpawn();
    }

    private void resetNextAirdropSpawn() {
        final int seconds = Integer.parseInt(configStore.getProperty("warzone.airdrop.timer"));
        this.nextAirdropSpawn = System.currentTimeMillis() + (1000L * seconds);
    }

    @Override
    public void run() {
        if(System.currentTimeMillis() < nextAirdropSpawn) {
            return;
        }
        if (service.getConfig().getEntity().getAirdrops().isEmpty()) {
            return;
        }
        final int minPlayerCount = Integer.parseInt(configStore.getProperty("warzone.airdrop.min.players"));
        if (Bukkit.getOnlinePlayers().size() < minPlayerCount) {
            return;
        }
        final Location location = UniqueRandomizer.getRandom("warzone.airdrop", service.getConfig().getEntity().getAirdrops());
        Bukkit.getScheduler().runTask(service.getGame(), () -> {
            service.getGame().getAirDropRegistry().callAirDrop(location);
        });
        resetNextAirdropSpawn();
    }
}
