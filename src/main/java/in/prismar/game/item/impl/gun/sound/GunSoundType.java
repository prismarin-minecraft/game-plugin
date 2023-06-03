package in.prismar.game.item.impl.gun.sound;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
public enum GunSoundType {

    SHOOT(true),
    RELOAD_IN(false),
    RELOAD_OUT(false),
    HIT(false),
    HEADSHOT(false),

    LOW_AMMO(false),

    AIM_IN(false),
    AIM_OUT(false),

    EQUIP(false);

    private final boolean surrounding;
}
