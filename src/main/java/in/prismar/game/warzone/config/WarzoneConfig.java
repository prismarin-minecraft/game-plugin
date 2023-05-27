package in.prismar.game.warzone.config;

import com.google.gson.GsonBuilder;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;

import java.util.ArrayList;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class WarzoneConfig extends GsonFileWrapper<WarzoneConfigData> {
    public WarzoneConfig(String dir) {
        super(dir.concat("warzone.json"), WarzoneConfig.class);
        load();
        if(getEntity() == null) {
            WarzoneConfigData data = new WarzoneConfigData();
            data.setAirdrops(new ArrayList<>());
            setEntity(data);
            save();
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
