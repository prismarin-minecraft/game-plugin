package in.prismar.game.command;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.playtime.PlaytimeProvider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserCacheProvider;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.SeasonData;
import in.prismar.game.Game;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.ffa.model.GameMap;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.SkullBuilder;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class StatsCommand extends SpigotCommand<Player> {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private static final ItemStack GLASS = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("§c ").build();

    @Inject
    private GameMapFacade mapFacade;

    public StatsCommand() {
        super("stats");
        setSenders(Player.class);
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
        UserCacheProvider cacheProvider = PrismarinApi.getProvider(UserCacheProvider.class);
        User user;
        if (arguments.getLength() >= 1) {
            String name = arguments.getString(0);
            if (!cacheProvider.existsByName(name)) {
                player.sendMessage(PrismarinConstants.PLAYER_NOT_FOUND_MESSAGE);
                return true;
            }
            UUID uuid = cacheProvider.getUUIDByName(name);
            user = provider.getUserByUUID(uuid);
        } else {
            user = provider.getUserByUUID(player.getUniqueId());
        }
        openMenu(player, user, PrismarinConstants.CURRENT_SEASON);
        return true;
    }


    private Frame openMenu(Player player, User user, String season) {
        PlaytimeProvider playtimeProvider = PrismarinApi.getProvider(PlaytimeProvider.class);

        Frame frame = new Frame("§cSeason " + season.replace("Season_", ""), 3);
        SeasonData data = user.getData().getSeasons().get(season);
        frame.fill();

        frame.addButton(10, new SkullBuilder(user.getData().getName()).setName("§b" + user.getData().getName()).build());

        final String dot = PrismarinConstants.LISTING_DOT + " §7";
        ItemStack general = new ItemBuilder(Material.BOOK).setName("§bGeneral")
                .addLore("§c")
                .addLore(dot + "Playtime§8: §b" + playtimeProvider.getOverallTimeInHours(user) + "h")
                .addLore(dot + "Balance§8: §b" + NumberFormatter.formatDoubleToThousands(data.getBalance()) + " $")
                .addLore(dot + "Battlepass§8: §b" + data.getBattlePass().getLevel())
                .addLore(dot + "Votes§8: §b" + data.getStats().getOrDefault("votes", 0L))
                .addLore("§c")
                .build();
        frame.addButton(12, general);

        ItemStack pvp = new ItemBuilder(Material.DIAMOND_SWORD).setName("§bPVP")
                .addLore("§c")
                .addLore(dot + "Kills§8: §b" + getStatsValue(user, season, "kills"))
                .addLore(dot + "Deaths§8: §b" + getStatsValue(user, season, "deaths"))
                .addLore(dot + "K/D§8: §b" + getKD(user, season, "kills", "deaths"))
                .addLore("§c")
                .addLore(dot + "Shots§8: §b" + getStatsValue(user, season, "shots"))
                .addLore(dot + "Hits§8: §b" + getStatsValue(user, season, "hits"))
                .addLore(dot + "Headshots§8: §b" + getStatsValue(user, season, "headshots"))
                .addLore(dot + "Accuracy§8: §b" + getAccuracy(user, season, "shots", "hits"))
                .addLore("§c")
                .allFlags()
                .build();

        frame.addButton(13, pvp);

        ItemStack maps = new ItemBuilder(Material.MAP).setName("§bMaps")
                .addLore("§c")
                .addLore("§7Click me to view all map statistics")
                .allFlags()
                .build();

        frame.addButton(14, maps, (ClickFrameButtonEvent) (clicker, event) -> {
            openMaps(player, user, season);
        });


        ItemStack history = new ItemBuilder(Material.CHEST).setName("§bStats History")
                .addLore("§c")
                .addLore("§7Click me to view all map statistics")
                .allFlags()
                .build();


        frame.addButton(16, history, (ClickFrameButtonEvent) (clicker, event) -> {
            openHistory(player, user, season);
        });


        frame.build();

        frame.openInventory(player, Sound.BLOCK_IRON_TRAPDOOR_OPEN, 0.5f);
        return frame;
    }

    private Frame openHistory(Player player, User user, String season) {
        Frame frame = new Frame("§cStats History", 2);
        for (int i = 9; i <= 16; i++) {
            frame.addButton(i, GLASS);
        }
        frame.addButton(17, new ItemBuilder(Material.OAK_DOOR).setName("§cBack").build(), (ClickFrameButtonEvent) (clicker, event) -> {
            openMenu(clicker, user, season);
        });

        int seasonNumber = Integer.valueOf(PrismarinConstants.CURRENT_SEASON.replace("Season_", ""));

        int slot = 0;
        for (int i = seasonNumber; i >= 0; i--) {
            ItemStack history = new ItemBuilder(Material.CHEST).setName("§bSeason " + seasonNumber)
                    .addLore("§c")
                    .addLore("§7Click me to view this season statistics")
                    .allFlags()
                    .build();
            frame.addButton(slot, history, (ClickFrameButtonEvent) (clicker, event) -> {
                openMenu(player, user, "" + seasonNumber);
            });
            slot++;
        }
        frame.build();
        frame.openInventory(player, Sound.UI_BUTTON_CLICK);
        return frame;
    }

    private Frame openMaps(Player player, User user, String season) {
        SeasonData data = user.getData().getSeasons().get(season);
        PlaytimeProvider playtimeProvider = PrismarinApi.getProvider(PlaytimeProvider.class);
        Frame frame = new Frame("§cSeason " + season.replace("Season_", ""), 2);
        for (int i = 9; i <= 16; i++) {
            frame.addButton(i, GLASS);
        }
        final String dot = PrismarinConstants.LISTING_DOT + " §7";
        int slot = 0;
        for (GameMap gameMap : mapFacade.getRepository().findAll()) {
            ItemStack map = new ItemBuilder(Material.MAP).setName("§b" + gameMap.getFancyName())
                    .addLore("§c")
                    .addLore(dot + "Kills§8: §b" + getStatsValue(user, season, "kills.map." + gameMap.getId()))
                    .addLore(dot + "Deaths§8: §b" + getStatsValue(user, season, "deaths.map." + gameMap.getId()))
                    .addLore(dot + "K/D§8: §b" + getKD(user, season, "kills.map." + gameMap.getId(), "deaths.map." + gameMap.getId()))
                    .addLore("§c").allFlags()
                    .build();
            frame.addButton(slot, map);
            slot++;
        }
        frame.addButton(17, new ItemBuilder(Material.OAK_DOOR).setName("§cBack").build(), (ClickFrameButtonEvent) (clicker, event) -> {
            openMenu(clicker, user, season);
        });
        frame.build();
        frame.openInventory(player, Sound.UI_BUTTON_CLICK);
        return frame;
    }

    private String getKD(User user, String season, String killsKey, String deathsKey) {
        SeasonData data = user.getData().getSeasons().get(season);
        long kills = data.getStats().getOrDefault(killsKey, 0L);
        long deaths = data.getStats().getOrDefault(deathsKey, 0L);
        double kd;
        try {
            kd = (double) kills / (double) deaths;
        }catch (ArithmeticException exception) {
            return "0.00";
        }
        return DECIMAL_FORMAT.format(kd);
    }

    private String getAccuracy(User user, String season, String shotsKey, String hitsKey) {
        SeasonData data = user.getData().getSeasons().get(season);
        long shots = data.getStats().getOrDefault(shotsKey, 0L);
        long hits = data.getStats().getOrDefault(hitsKey, 0L);
        double percentage;
        try {
            percentage = ((double) hits / (double) shots) * 100.0;
        }catch (ArithmeticException exception) {
            return "0%";
        }
        return DECIMAL_FORMAT.format(percentage) + "%";
    }

    private String getStatsValue(User user, String season, String key) {
        SeasonData data = user.getData().getSeasons().get(season);
        return NumberFormatter.formatNumberToThousands(data.getStats().getOrDefault(key, 0L));
    }


}
