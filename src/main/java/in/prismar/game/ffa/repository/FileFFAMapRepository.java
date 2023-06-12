package in.prismar.game.ffa.repository;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import in.prismar.game.ffa.model.FFAMap;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import in.prismar.library.spigot.item.container.ItemContainer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FileFFAMapRepository extends GsonFileWrapper<Map<String, FFAMap>> implements FFAMapRepository {


    public FileFFAMapRepository(String directory) {
        super(directory.concat("maps.json"), new TypeToken<Map<String, FFAMap>>(){}.getType());
        load();
        if(getEntity() == null) {
            setEntity(new HashMap<>());
        } else {
            for(FFAMap map : getEntity().values()) {
                map.getIcon().deserialize();
                map.setPlayers(new HashMap<>());
            }
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }

    @Override
    public FFAMap create(String id, ItemStack icon) {
        FFAMap map = new FFAMap();
        map.setId(id);
        map.setIcon(new ItemContainer(icon));
        map.setSpawns(new ArrayList<>());
        map.setPowerUps(new ArrayList<>());
        map.setPlayers(new HashMap<>());
        return create(map);
    }

    @Override
    public FFAMap create(FFAMap map) {
        map.setId(map.getId().toLowerCase());
        getEntity().put(map.getId(), map);
        save();
        return map;
    }

    @Override
    public FFAMap findById(String id) {
        return getEntity().get(id.toLowerCase());
    }

    @Override
    public Optional<FFAMap> findByIdOptional(String s) {
        return Optional.empty();
    }

    @Override
    public Collection<FFAMap> findAll() {
        return getEntity().values();
    }

    @Override
    public boolean existsById(String id) {
        return getEntity().containsKey(id.toLowerCase());
    }

    @Override
    public FFAMap save(FFAMap map) {
        save();
        return map;
    }

    @Override
    public FFAMap delete(FFAMap map) {
        getEntity().remove(map.getId());
        return map;
    }

    @Override
    public FFAMap deleteById(String id) {
        if(getEntity().containsKey(id.toLowerCase())) {
            FFAMap map = getEntity().get(id.toLowerCase());
            getEntity().remove(map.getId());
            return map;
        }
        return null;
    }
}
