package in.prismar.game.stats;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.SeasonData;
import in.prismar.library.common.math.MathUtil;
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

    private final ConfigStore store;

    public GameStatsDistributor() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
        this.store = PrismarinApi.getProvider(ConfigStore.class);
    }

    public int addFFAKillMoney(Player player) {
        String[] stringMoney = store.getProperty("ffa.money.kill").split("-");
        int money = MathUtil.random(Integer.valueOf(stringMoney[0]), Integer.valueOf(stringMoney[1]));
        addMoney(player, money);
        return money;
    }

    public int addFFAKillstreakMoney(Player player, int streak) {
        String[] messages = store.getProperty("ffa.money.killstreak").split("/");
        String chosenMessage = null;
        for(String message : messages) {
            if(message.contains("{" + streak + "}")) {
                chosenMessage = message;
                break;
            }
        }
        if(chosenMessage != null) {
            chosenMessage = chosenMessage.replace("{" + streak + "}", "");
            int money = Integer.valueOf(chosenMessage);
            addMoney(player, money);
            return money;
        }
        return -1;
    }

    public void addMoney(Player player, double balance) {
        User user = provider.getUserByUUID(player.getUniqueId());
        SeasonData data = user.getSeasonData();
        data.setBalance(data.getBalance() + balance);
        provider.saveAsync(user, true);
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
