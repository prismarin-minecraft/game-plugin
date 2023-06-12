package in.prismar.game.stats;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.battlepass.BattlePassProvider;
import in.prismar.api.booster.BoosterProvider;
import in.prismar.api.booster.BoosterType;
import in.prismar.api.clan.ClanStatsProvider;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.SeasonData;
import in.prismar.game.ffa.model.GameMap;
import in.prismar.game.ffa.model.GameMapPlayer;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.meta.anno.Service;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.function.Consumer;

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

    private ClanStatsProvider clanStatsProvider;


    private final ConfigStore store;

    public GameStatsDistributor() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
        this.store = PrismarinApi.getProvider(ConfigStore.class);
    }

    public void displayStreak(Player player, int streak, Consumer<String> consumer) {
        String[] messages = store.getProperty("killstreak.messages").split("/");
        String chosenMessage = null;
        for(String message : messages) {
            if(message.contains("{" + streak + "}")) {
                chosenMessage = message;
                break;
            }
        }
        if(chosenMessage != null) {
            chosenMessage = chosenMessage.replace("{" + streak + "}", "")
                    .replace("{player}", player.getName()).replace("&", "§");
            consumer.accept(chosenMessage);
        }
    }

    public String getRandomDeathMessage(Player killer, Player target) {
        String[] messages = store.getProperty("death.messages").split("/");
        String random = messages[MathUtil.random(messages.length - 1)];
        return random.replace("&", "§").replace("{killer}", killer.getName())
                .replace("{target}", target.getName());
    }

    public int addFFABattlePassEXP(Player player) {
        BoosterProvider boosterProvider = PrismarinApi.getProvider(BoosterProvider.class);
        String[] split = store.getProperty("ffa.exp.kill").split("-");
        int exp = MathUtil.random(Integer.valueOf(split[0]), Integer.valueOf(split[1])) * boosterProvider.getMultiplier(BoosterType.BATTLEPASS);
        addBattlePassEXP(player, exp);
        return exp;
    }

    public void addBattlePassEXP(Player player, long exp) {
        BattlePassProvider battlePassProvider = PrismarinApi.getProvider(BattlePassProvider.class);
        battlePassProvider.addExp(provider.getUserByUUID(player.getUniqueId()), exp);
    }

    public int addFFAKillMoney(Player player) {
        BoosterProvider boosterProvider = PrismarinApi.getProvider(BoosterProvider.class);
        String[] stringMoney = store.getProperty("ffa.money.kill").split("-");
        int money = MathUtil.random(Integer.valueOf(stringMoney[0]), Integer.valueOf(stringMoney[1])) * boosterProvider.getMultiplier(BoosterType.MONEY);
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
            BoosterProvider boosterProvider = PrismarinApi.getProvider(BoosterProvider.class);
            chosenMessage = chosenMessage.replace("{" + streak + "}", "");
            int money = Integer.valueOf(chosenMessage) * boosterProvider.getMultiplier(BoosterType.MONEY);
            addMoney(player, money * boosterProvider.getMultiplier(BoosterType.MONEY));
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

        long currentHighestStreak = user.getSeasonData().getStats().getOrDefault("highest.killstreak", 0L);
        if(next > currentHighestStreak) {
            user.getSeasonData().getStats().put("highest.killstreak", (long)next);
        }

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
        getClanStatsProvider().addKill(player);
        provider.saveAsync(user, true);
    }

    public void addDeath(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("deaths", getCurrentValue(data, "deaths") + 1);
        getClanStatsProvider().addDeath(player);
        provider.saveAsync(user, true);
    }

    public void addMapKill(Player player, String map) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("kills.map." + map, getCurrentValue(data, "kills.map." + map) + 1);
        provider.saveAsync(user, true);
    }

    public void addExtractionKill(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("kills.extraction", getCurrentValue(data, "kills.extraction") + 1);
        provider.saveAsync(user, true);
    }

    public void addExtractionDeath(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("deaths.extraction", getCurrentValue(data, "deaths.extraction") + 1);
        provider.saveAsync(user, true);
    }

    public void addHardpointKill(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("kills.hardpoint", getCurrentValue(data, "kills.hardpoint") + 1);
        getClanStatsProvider().addKill(player);
        provider.saveAsync(user, true);
    }

    public void addWarzoneKill(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("kills.warzone", getCurrentValue(data, "kills.warzone") + 1);
        provider.saveAsync(user, true);
    }

    public void addWarzoneDeath(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("deaths.warzone", getCurrentValue(data, "deaths.warzone") + 1);
        provider.saveAsync(user, true);
    }

    public void addHardpointDeath(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        checkForNullStats(user);
        SeasonData data = user.getSeasonData();
        data.getStats().put("deaths.hardpoint", getCurrentValue(data, "deaths.hardpoint") + 1);
        getClanStatsProvider().addDeath(player);
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

    public ClanStatsProvider getClanStatsProvider() {
        if(clanStatsProvider == null) {
            clanStatsProvider = PrismarinApi.getProvider(ClanStatsProvider.class);
        }
        return clanStatsProvider;
    }
}
