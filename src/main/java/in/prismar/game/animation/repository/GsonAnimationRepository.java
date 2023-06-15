package in.prismar.game.animation.repository;

import com.google.gson.GsonBuilder;
import in.prismar.game.Game;
import in.prismar.game.animation.model.Animation;
import in.prismar.game.animation.model.AnimationFrame;
import in.prismar.game.animation.model.AnimationFrameBlock;
import in.prismar.library.file.gson.GsonRepository;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import in.prismar.library.spigot.location.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GsonAnimationRepository extends GsonRepository<Animation> implements AnimationRepository {

    public GsonAnimationRepository(Game game) {
        super(game.getDefaultDirectory() + "animations" + File.separator, Animation.class, "Animation", 10000);
        loadAll();
        for(Animation animation : findAll()) {
            animation.setCuboid(new Cuboid(animation.getLocationA(), animation.getLocationB()));
            animation.setTempData(new HashMap<>());
            for(AnimationFrame frame : animation.getFrames()) {
                for(AnimationFrameBlock block : frame.getBlocks()) {
                    block.setBlockData(Bukkit.createBlockData(block.getBlockDataValue()));
                }
            }
        }
    }

    @Override
    public Collection<Animation> getAnimations() {
        return findAll();
    }

    @Override
    public void interceptEntry(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
