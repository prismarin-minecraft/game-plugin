package in.prismar.game.hardpoint.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HardpointConfigFile extends GsonFileWrapper<HardpointConfig> {
    public HardpointConfigFile(String directory) {
        super(directory.concat("hardpoint.json"), new TypeToken<HardpointConfig>() {
        }.getType());
        load();
        if (getEntity() == null) {
            HardpointConfig config = new HardpointConfig();
            config.setSpawns(new HashMap<>());
            for (HardpointTeam team : HardpointTeam.values()) {
                config.getSpawns().put(team, new ArrayList<>());
            }
            config.setPoints(new ArrayList<>());
            config.setMaxSpaceBetweenTeams(2);
            setEntity(config);
            save();
        } else {
            for (Hardpoint hardpoint : getEntity().getPoints()) {
                hardpoint.setCircle(generateCircle(hardpoint.getLocation()));
            }
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }

    public List<Location> generateCircle(Location loc) {
        List<Location> locations = new ArrayList<>();
        int r = 5;
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        World w = loc.getWorld();
        int rSquared = r * r;
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared) {
                    locations.add(w.getBlockAt(x, cy, z).getLocation());
                }
            }
        }
        return locations;
    }
}
