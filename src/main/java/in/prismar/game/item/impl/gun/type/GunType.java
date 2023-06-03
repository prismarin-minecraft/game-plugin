package in.prismar.game.item.impl.gun.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
public enum GunType {

    PISTOL("Pistol", Material.WOODEN_HOE),
    SMG("SMG", Material.WOODEN_AXE),
    SHOTGUN("Shotgun", Material.WOODEN_SHOVEL),
    AR("Assault Rifle", Material.WOODEN_PICKAXE),
    SNIPER("Sniper", Material.DIAMOND_HOE),

    LMG("LMG", Material.STONE_AXE),

    SPECIAL("Special", Material.DIAMOND_AXE);

    private final String displayName;
    private final Material material;

    public static GunType getGunTypeByDisplayName(String text) {
        for(GunType type : GunType.values()) {
            if(type.getDisplayName().equalsIgnoreCase(text.replace("_", " "))) {
                return type;
            }
        }
        return null;
    }
}
