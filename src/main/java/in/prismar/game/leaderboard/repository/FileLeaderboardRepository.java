package in.prismar.game.leaderboard.repository;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import in.prismar.game.leaderboard.model.Leaderboard;
import in.prismar.library.file.gson.GsonCompactRepository;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FileLeaderboardRepository extends GsonCompactRepository<Leaderboard> implements LeaderboardRepository {
    public FileLeaderboardRepository(String directory) {
        super(directory.concat("leaderboards.json"), new TypeToken<Map<String, Leaderboard>>(){});
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }

    @Override
    public Leaderboard create(String id, Location location, String sorted, int size, String title, String format) {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setId(id);
        leaderboard.setLocation(location);
        leaderboard.setSorted(sorted);
        leaderboard.setSize(size);
        leaderboard.setTitle(title);
        leaderboard.setFormat(format);
        return create(leaderboard);
    }
}
