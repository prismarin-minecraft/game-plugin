package in.prismar.game.item.impl.gun.type;

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
public enum GunType {

    PISTOL("Pistol"),
    SMG("SMG"),
    SHOTGUN("Shotgun"),
    AR("Assault Rifle"),
    SNIPER("Sniper"),

    LMG("LMG");

    private final String displayName;
}
