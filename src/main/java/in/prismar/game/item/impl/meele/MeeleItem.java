package in.prismar.game.item.impl.meele;

import in.prismar.game.Game;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Setter
public class MeeleItem extends CustomItem {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0");

    private double damage;
    private double attackSpeed = 1.6;

    public MeeleItem(String id, Material material, String displayName) {
        super(id, material, displayName);
        //allFlags();
        generateDefaultLore();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, EntityDamageByEntityEvent event) {
        if (holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND) {
            return;
        }
        event.setDamage(damage);
    }

    protected void generateDefaultLore() {
        setLore(buildDefaultLore());
    }

    protected List<String> buildDefaultLore() {
        List<String> lore = new ArrayList<>();
        lore.add("§c ");
        lore.add(" §7Damage§8: §b" + DECIMAL_FORMAT.format(damage));
        lore.add("§c ");
        return lore;
    }

}
