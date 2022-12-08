package in.prismar.game.extraction.map;

import com.google.common.reflect.TypeToken;
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
public class ExtractionMapFile extends GsonFileWrapper<ExtractionMap> {

    public ExtractionMapFile(String directory) {
        super(directory.concat("extraction.json"), new TypeToken<ExtractionMap>(){}.getType());
        load();
        if(getEntity() == null) {
            ExtractionMap map = new ExtractionMap();
            map.setSpawns(new ArrayList<>());
            setEntity(map);
            save();
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
