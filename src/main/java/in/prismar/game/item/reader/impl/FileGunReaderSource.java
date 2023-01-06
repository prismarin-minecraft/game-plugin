package in.prismar.game.item.reader.impl;

import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.reader.CustomItemReaderSource;
import in.prismar.library.file.toml.TomlConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FileGunReaderSource implements CustomItemReaderSource {
    @Override
    public List<CustomItem> read(CustomItemRegistry registry) {
        List<CustomItem> items = new ArrayList<>();
        File directory = new File(registry.getGame().getDefaultDirectory() + "items" + File.separator + "guns" + File.separator);
        for(File file : directory.listFiles()) {
            TomlConfig<GunData> tomlConfig = new TomlConfig<>(file.getAbsolutePath(), GunData.class);
            if(tomlConfig.getEntity() != null) {
                GunData data = tomlConfig.getEntity();
                Gun gun = new Gun(data.getId(), data.getType(), data.getMaterial(), data.getDisplayName());
                gun.setCustomModelData(data.getCustomModelData());
                if(data.getShootParticle() != null) {
                    gun.setShootParticle(data.getShootParticle());
                }
                gun.setAmmoType(data.getAmmoType());
                gun.setRange(data.getRange());
                gun.setFireRate(data.getFireRate());
                gun.setSpread(data.getSpread());
                gun.setSneakSpread(data.getSneakSpread());
                gun.setBulletsPerShot(data.getBulletsPerShot());
                gun.setMaxAmmo(data.getMaxAmmo());
                gun.setReloadTimeInTicks(data.getReloadTimeInTicks());
                gun.setLegDamage(data.getLegDamage());
                gun.setBodyDamage(data.getBodyDamage());
                gun.setHeadDamage(data.getHeadDamage());
                gun.setZoom(data.getZoom());

                if(!data.getSounds().isEmpty()) {
                    for(GunSoundData soundData : data.getSounds()) {
                        GunSound gunSound = new GunSound(null, (float)soundData.getVolume(), (float)soundData.getPitch());
                        gunSound.setSoundName(soundData.getSound());
                        gunSound.setSurroundingDistance(soundData.getSurroundingDistance());
                        gun.getSounds().put(soundData.getType(), gunSound);
                    }
                }
                gun.generateDefaultLore();
                items.add(gun);
            }
        }

        return items;
    }
}
