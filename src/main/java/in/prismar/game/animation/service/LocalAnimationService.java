package in.prismar.game.animation.service;

import in.prismar.game.animation.AnimationFacade;
import in.prismar.game.animation.model.Animation;
import in.prismar.game.animation.model.AnimationFrame;
import in.prismar.game.animation.model.AnimationFrameBlock;
import in.prismar.library.spigot.location.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/

public class LocalAnimationService implements AnimationService{

    private final AnimationFacade facade;

    public LocalAnimationService(AnimationFacade facade) {
        this.facade = facade;
    }

    @Override
    public Animation create(String id, int ticks, Location locationA, Location locationB) {
        Animation animation = new Animation();
        animation.setId(id.toLowerCase());
        animation.setTicks(ticks);
        animation.setLocationA(locationA);
        animation.setLocationB(locationB);
        animation.setFrames(new ArrayList<>());
        animation.setCuboid(new Cuboid(locationA, locationB));
        animation.setTempData(new HashMap<>());
        facade.getRepository().create(animation);
        return animation;
    }

    @Override
    public void delete(String id) {
        facade.getRepository().deleteById(id.toLowerCase());
    }

    @Override
    public void play(Animation animation, boolean reverse) {
        new BukkitRunnable() {
            int frame = !reverse ? 0 : animation.getFrames().size()-1;
            @Override
            public void run() {
                try {
                    int size = frame + 1;
                    boolean close = reverse ? (size < 0) : (size > animation.getFrames().size());
                    if(close) {
                        cancel();
                        if(!animation.isStay()) {
                            changeToFrame(animation, !reverse ? 0 : animation.getFrames().size()-1);
                            return;
                        }
                        return;
                    }
                    changeToFrame(animation, frame);
                    if(reverse) {
                        frame--;
                    } else {
                        frame++;
                    }
                }catch (IndexOutOfBoundsException exception) {
                    cancel();
                }

            }
        }.runTaskTimer(facade.getGame(), animation.getTicks(), animation.getTicks());
    }

    @Override
    public void setStay(Animation animation, boolean stay) {
        animation.setStay(stay);
        facade.getRepository().save(animation);
    }

    @Override
    public void changeTicks(Animation animation, int ticks) {
        animation.setTicks(ticks);
        facade.getRepository().save(animation);
    }

    @Override
    public AnimationFrame createFrame(Animation animation) {
        AnimationFrame frame = new AnimationFrame(new ArrayList<>());
        Iterator<Block> blocks = animation.getCuboid().blockList();
        while (blocks.hasNext()) {
            Block block = blocks.next();
            frame.getBlocks().add(new AnimationFrameBlock(block.getLocation(), block.getType()));
        }
        return frame;
    }

    @Override
    public AnimationFrame addFrame(Animation animation) {
        AnimationFrame frame = createFrame(animation);
        animation.getFrames().add(frame);
        facade.getRepository().save(animation);
        return frame;
    }

    @Override
    public void removeFrame(Animation animation, int index) {
        animation.getFrames().remove(index);
        facade.getRepository().save(animation);
    }

    @Override
    public AnimationFrame changeToFrame(Animation animation, int index) {
        AnimationFrame frame = getFrame(animation, index);
        for(AnimationFrameBlock block : frame.getBlocks()) {
            Block bukkitBlock = block.getLocation().getBlock();
            bukkitBlock.setType(block.getMaterial());
            bukkitBlock.setBlockData(block.getBlockData());
        }
        return frame;
    }

    @Override
    public AnimationFrame saveFrame(Animation animation, int index) {
        AnimationFrame frame = createFrame(animation);
        animation.getFrames().remove(index);
        animation.getFrames().add(index, frame);
        facade.getRepository().save(animation);
        return frame;
    }

    @Override
    public AnimationFrame getFrame(Animation animation, int index) {
        return animation.getFrames().get(index);
    }

    @Override
    public boolean existsFrame(Animation animation, int index) {
        int size = index+1;
        return size <= animation.getFrames().size();
    }
}
