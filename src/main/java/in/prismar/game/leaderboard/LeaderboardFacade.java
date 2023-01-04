package in.prismar.game.leaderboard;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.SeasonData;
import in.prismar.api.user.data.UserData;
import in.prismar.game.Game;
import in.prismar.game.leaderboard.model.Leaderboard;
import in.prismar.game.leaderboard.repository.FileLeaderboardRepository;
import in.prismar.game.leaderboard.repository.LeaderboardRepository;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class LeaderboardFacade {

    private LeaderboardRepository repository;

    public LeaderboardFacade(Game game) {
        this.repository = new FileLeaderboardRepository(game.getDefaultDirectory());
        Bukkit.getScheduler().runTaskTimer(game, () -> {
            for(Leaderboard leaderboard : repository.findAll()) {
                update(leaderboard);
            }
        }, 0, 20 * 60 * 5);
    }

    public void despawn(Leaderboard leaderboard) {
        if(leaderboard.getHologram() != null) {
            leaderboard.getHologram().disable();
        }
    }

    public void update(Leaderboard leaderboard) {
        despawn(leaderboard);
        UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);

        String fullSorted = "seasons." + PrismarinConstants.CURRENT_SEASON + ".stats." + leaderboard.getSorted();
        if(leaderboard.getSorted().equalsIgnoreCase("balance")) {
            fullSorted = "seasons." + PrismarinConstants.CURRENT_SEASON + ".balance";
        }
        userProvider.getAllSorted(fullSorted, 0, leaderboard.getSize()).thenAccept(data -> {
            Hologram hologram = new Hologram(leaderboard.getLocation());
            hologram.addLine(HologramLineType.TEXT, PrismarinConstants.BORDER);
            hologram.addLine(HologramLineType.TEXT, "§6Leaderboard §8| §b" + leaderboard.getTitle());
            hologram.addLine(HologramLineType.TEXT, "§c ");
            for (int i = 0; i < leaderboard.getSize(); i++) {
                if(data.size() >= i+1) {
                    UserData userData = data.get(i);
                    long amount = 0;
                    if(userData.getSeasons().containsKey(PrismarinConstants.CURRENT_SEASON)) {
                        SeasonData seasonData = userData.getSeasons().get(PrismarinConstants.CURRENT_SEASON);
                        if(leaderboard.getSorted().equalsIgnoreCase("balance")) {
                            amount = (long)seasonData.getBalance();
                        } else {
                            if(seasonData.getStats() != null) {
                                if(seasonData.getStats().containsKey(leaderboard.getSorted())) {
                                    amount = seasonData.getStats().get(leaderboard.getSorted());
                                }

                            }
                        }

                    }
                    final String color = i == 0 ? "§b" : i == 1 ? "§6" : i == 2 ? "§3" : "§7";
                    final String text = leaderboard.getFormat().replace("%player%", userData.getName())
                            .replace("%amount%", NumberFormatter.formatNumberToThousands(amount)).replace("%place%", (i+1) + "")
                            .replace("%color%", color);
                    hologram.addLine(HologramLineType.TEXT, text);

                }
            }
            hologram.addLine(HologramLineType.TEXT, PrismarinConstants.BORDER);
            hologram.enable();
            leaderboard.setHologram(hologram);
        }).exceptionally(throwable -> {
            throwable.printStackTrace();
            return null;
        });
    }
}
