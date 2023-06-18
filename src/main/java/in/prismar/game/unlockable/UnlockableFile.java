package in.prismar.game.unlockable;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/

public class UnlockableFile extends GsonFileWrapper<Map<String, Unlockable>> {
    public UnlockableFile(String directory) {
        super(directory.concat("unlockables.json"), new TypeToken<Map<String, Unlockable>>(){}.getType());
        load();
        if(getEntity() == null) {
            setEntity(new HashMap<>());
        }
    }



    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
