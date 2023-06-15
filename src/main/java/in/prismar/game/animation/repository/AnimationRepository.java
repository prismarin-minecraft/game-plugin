package in.prismar.game.animation.repository;


import in.prismar.game.animation.model.Animation;
import in.prismar.library.common.repository.Repository;

import java.util.Collection;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface AnimationRepository extends Repository<String, Animation> {


    Collection<Animation> getAnimations();
}
