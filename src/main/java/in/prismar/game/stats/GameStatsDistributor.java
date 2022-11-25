package in.prismar.game.stats;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.SeasonData;
import in.prismar.library.meta.anno.Service;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class GameStatsDistributor {

    private static final String KILLSTREAK_TAG = "killstreak";

    private final UserProvider<User> provider;

    public GameStatsDistributor() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
    }

    public int getKillstreak(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        return user.containsTag(KILLSTREAK_TAG) ? user.getTag(KILLSTREAK_TAG) : 0;
    }

    public int addKillstreak(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        int next = getKillstreak(player) + 1;
        user.setTag(KILLSTREAK_TAG, next);
        return next;
    }

    public void resetKillstreak(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        user.removeTag(KILLSTREAK_TAG);
    }

    public void addKill(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("kills", getCurrentValue(data, "kills") + 1);
        provider.saveAsync(user, true);
    }

    public void addDeath(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("deaths", getCurrentValue(data, "deaths") + 1);
        provider.saveAsync(user, true);
    }

    public void addMapKill(Player player, String map) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("kills.map." + map, getCurrentValue(data, "kills.map." + map) + 1);
        provider.saveAsync(user, true);
    }

    public void addMapDeath(Player player, String map) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("deaths.map." + map, getCurrentValue(data, "deaths.map." + map) + 1);
        provider.saveAsync(user, true);
    }

    private long getCurrentValue(SeasonData data, String key) {
        return data.getStats().containsKey(key) ? data.getStats().get(key) : 0;
    }

    private void checkForNullStats(User user) {
        if(user.getSeasonData().getStats() == null) {
            user.getSeasonData().setStats(new HashMap<>());
        }
    }
}
