package in.prismar.game.item.impl.armor;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.model.CustomItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerItemHeldEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class ArmorItem extends CustomItem {

    protected final ArmorType type;

    private int headProtection = 10;
    private int bodyProtection = 10;

    private boolean custom;

    public ArmorItem(String id, Material material, String displayName, ArmorType type) {
        super(id, material, displayName);
        this.type = type;
        allFlags();
        setUnbreakable(true);
    }

    protected void generateDefaultLore(){
        addLore("§c ");
        addLore(" §8╔══ §dProtection");
        addLore(" §8╠ §7Head§8: §b" + headProtection + "%");
        addLore(" §8╠ §7Body§8: §b" + bodyProtection + "%");
        addLore(" §8╚══");
        addLore("§c ");
    }

}
