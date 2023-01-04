package in.prismar.game.leaderboard.repository;

import in.prismar.game.leaderboard.model.Leaderboard;
import in.prismar.library.common.repository.Repository;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface LeaderboardRepository extends Repository<String, Leaderboard> {

    Leaderboard create(String id, Location location, String sorted, int size, String title, String format);
}
