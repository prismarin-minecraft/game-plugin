package in.prismar.game.item.reader.impl;

import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.impl.gun.type.GunType;
import in.prismar.game.item.model.Skin;
import in.prismar.game.item.reader.BaseReaderData;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
@NoArgsConstructor
public class GunData extends BaseReaderData {


    private GunType type;
    private Particle shootParticle;

    private AmmoType ammoType;

    private double range;

    private int fireRate;

    private double spread;
    private double sneakSpread;
    private int bulletsPerShot;

    private int maxAmmo;

    private int reloadTimeInTicks;

    private double legDamage;
    private double bodyDamage;
    private double headDamage;

    private int zoom;

    private String previewImage;
    private List<Skin> skins;

    private List<GunSoundData> sounds = new ArrayList<>();


}
