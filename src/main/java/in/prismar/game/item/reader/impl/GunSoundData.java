package in.prismar.game.item.reader.impl;

import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import lombok.Data;
import org.bukkit.Sound;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class GunSoundData {

    private GunSoundType type;
    private Sound sound;
    private double volume;
    private double pitch;
    private double surroundingDistance;
}
