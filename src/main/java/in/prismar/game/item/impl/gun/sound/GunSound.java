package in.prismar.game.item.impl.gun.sound;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Sound;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@NoArgsConstructor
public class GunSound {

    private Sound sound;
    private String soundName;
    private float volume;
    private float pitch;

    private double surroundingDistance = 20;

    public GunSound(Sound sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
}
