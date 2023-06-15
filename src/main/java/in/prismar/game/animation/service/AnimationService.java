package in.prismar.game.animation.service;

import in.prismar.game.animation.model.Animation;
import in.prismar.game.animation.model.AnimationFrame;
import org.bukkit.Location;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface AnimationService {

    Animation create(String id, int ticks, Location locationA, Location locationB);
    void delete(String id);

    void play(Animation animation, boolean reverse);

    void changeTicks(Animation animation, int ticks);
    void setStay(Animation animation, boolean stay);

    AnimationFrame createFrame(Animation animation);
    AnimationFrame addFrame(Animation animation);


    void removeFrame(Animation animation, int index);
    AnimationFrame changeToFrame(Animation animation, int index);
    AnimationFrame saveFrame(Animation animation, int index);
    boolean existsFrame(Animation animation, int index);
    AnimationFrame getFrame(Animation animation, int index);
}
