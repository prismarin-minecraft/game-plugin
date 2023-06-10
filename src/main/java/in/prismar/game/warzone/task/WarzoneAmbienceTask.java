package in.prismar.game.warzone.task;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/

public class WarzoneAmbienceTask implements Runnable {


    private final WarzoneService warzoneService;
    private Map<Player, Long> ambience;
    private ConfigStore configStore;

    public WarzoneAmbienceTask(WarzoneService warzoneService) {
        this.warzoneService = warzoneService;
        this.ambience = new HashMap<>();
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @Override
    public void run() {
        for(Player online : Bukkit.getOnlinePlayers()) {
            if(!warzoneService.isInWarzone(online)) {
                if(ambience.containsKey(online)) {
                    online.stopSound("ambience.wind");
                    ambience.remove(online);
                }
                continue;
            }
            if(ambience.containsKey(online)) {
                long until = ambience.get(online);
                if(System.currentTimeMillis() >= until) {
                    online.playSound(online.getLocation(), "ambience.wind", SoundCategory.AMBIENT, Float.parseFloat(configStore.getProperty("warzone.ambience.volume")), 1);
                    ambience.put(online, System.currentTimeMillis() + getNextPlayTime());
                }
            } else {
                online.playSound(online.getLocation(), "ambience.wind", SoundCategory.AMBIENT, Float.parseFloat(configStore.getProperty("warzone.ambience.volume")), 1);
                ambience.put(online, System.currentTimeMillis() + getNextPlayTime());
            }
        }
    }

    public long getNextPlayTime() {
        return Long.parseLong(configStore.getProperty("warzone.ambience.repeat"));
    }
}
