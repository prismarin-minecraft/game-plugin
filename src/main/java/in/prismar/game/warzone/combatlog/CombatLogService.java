package in.prismar.game.warzone.combatlog;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.region.RegionProvider;
import in.prismar.game.Game;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class CombatLogService {

    private final ConfigStore configStore;

    private final RegionProvider regionProvider;

    private Map<UUID, CombatLog> combatLogs;

    public CombatLogService(Game game) {
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
        this.combatLogs = new ConcurrentHashMap<>();
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new CombatLogTask(this), 20, 20);
    }

    public void removeCombatLog(Player player) {
        this.combatLogs.remove(player.getUniqueId());
    }

    public void clear() {
        this.combatLogs.clear();
    }

    public boolean addCombatLog(Player player, Player target) {
        boolean exists = combatLogs.containsKey(player.getUniqueId());
        CombatLog log = exists ? combatLogs.get(player.getUniqueId()) : new CombatLog(player, target);
        log.setTarget(target);
        log.setUntil(System.currentTimeMillis() + getUntilTime());
        if(!exists) {
            combatLogs.put(player.getUniqueId(), log);
        }
        return exists;
    }

    public boolean isInCombatLog(Player player) {
        return this.combatLogs.containsKey(player.getUniqueId());
    }

    public long getUntilTime() {
        return Long.parseLong(configStore.getProperty("warzone.combatlog.time"));
    }
}
