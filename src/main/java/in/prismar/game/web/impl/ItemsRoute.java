package in.prismar.game.web.impl;

import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.reader.impl.GunData;
import in.prismar.game.web.route.GetWebRoute;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ItemsRoute extends GetWebRoute<List<GunData>> {

    private final CustomItemRegistry registry;
    public ItemsRoute(CustomItemRegistry registry) {
        super("items");
        this.registry = registry;
    }

    @Override
    public List<GunData> onRoute(Context context) {
        List<GunData> list = new ArrayList<>();
        for(CustomItem customItem : registry.getItems().values()) {
            if(customItem instanceof Gun gun) {
                GunData data = new GunData();
                data.setId(gun.getId());
                data.setDisplayName(gun.getDisplayName());
                data.setMaterial(gun.getMaterial());
                data.setCustomModelData(gun.getCustomModelData());

                data.setType(gun.getType());
                data.setAmmoType(gun.getAmmoType());
                data.setRange(gun.getRange());

                data.setFireRate(gun.getFireRate());
                data.setSpread(gun.getSpread());
                data.setSneakSpread(gun.getSneakSpread());
                data.setBulletsPerShot(gun.getBulletsPerShot());
                data.setMaxAmmo(gun.getMaxAmmo());
                data.setReloadTimeInTicks(gun.getReloadTimeInTicks());
                data.setLegDamage(gun.getLegDamage());
                data.setBodyDamage(gun.getBodyDamage());
                data.setHeadDamage(gun.getHeadDamage());
                data.setZoom(gun.getZoom());
                data.setPreviewImage(gun.getPreviewImage());
                data.setSkins(gun.getSkins());

                list.add(data);
            }
        }
        return list;
    }
}
