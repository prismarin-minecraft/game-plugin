package in.prismar.game.item.reader.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.sound.GunSound;
import in.prismar.game.item.impl.gun.sound.GunSoundType;
import in.prismar.game.item.reader.CustomItemReaderSource;
import org.bukkit.Sound;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GunReaderSource implements CustomItemReaderSource {

    private final ConfigStore store;
    private final Gson gson;

    public GunReaderSource() {
        this.store = PrismarinApi.getProvider(ConfigStore.class);
        this.gson = new GsonBuilder().create();
    }

    @Override
    public List<CustomItem> read(CustomItemRegistry registry) {
        final String json = store.getProperty("guns");
        if(json != null) {
            List<CustomItem> items = new ArrayList<>();
            List<GunData> list = gson.fromJson(json, new TypeToken<List<GunData>>(){}.getType());
            for(GunData data : list) {
                Gun gun = new Gun(data.getId(), data.getType(), data.getMaterial(), data.getDisplayName());
                if(data.getShootParticle() != null) {
                    gun.setShootParticle(data.getShootParticle());
                }
                gun.setAmmoType(data.getAmmoType());

                gun.setRange(data.getRange());
                gun.setFireRate(data.getFireRate());
                gun.setSpread(data.getSpread());
                gun.setBulletsPerShot(data.getBulletsPerShot());
                gun.setMaxAmmo(data.getMaxAmmo());
                gun.setReloadTimeInTicks(data.getReloadTimeInTicks());
                gun.setLegDamage(data.getLegDamage());
                gun.setBodyDamage(data.getBodyDamage());
                gun.setHeadDamage(data.getHeadDamage());

                if(data.getSounds() != null) {
                    for(Map.Entry<GunSoundType, List<GunSound>> entry : data.getSounds().entrySet()) {
                        gun.getSounds().put(entry.getKey(), entry.getValue());
                    }
                }
                gun.generateDefaultLore();
                items.add(gun);
            }
            return items;
        }
        return Collections.emptyList();
    }

}
