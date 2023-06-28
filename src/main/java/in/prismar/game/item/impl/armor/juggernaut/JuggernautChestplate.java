package in.prismar.game.item.impl.armor.juggernaut;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.armor.ArmorType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class JuggernautChestplate extends ArmorItem {
    public JuggernautChestplate() {
        super("JuggernautChestplate", Material.NETHERITE_CHESTPLATE, "§4Juggernaut Chestplate", ArmorType.CHESTPLATE);
        setHeadProtection(0);
        setBodyProtection(30);

        generateDefaultLore();

        addLore(" §7Effects§8:");
        addLore("   §8➥ §cSlowness 2");
        addLore("§c ");
    }

    @CustomItemEvent
    public void onUpdate(Player player, Game game, CustomItemHolder holder, CustomItemHolder event) {
        if(holder.getHoldingType() != CustomItemHoldingType.ARMOR) {
            return;
        }
        if(!player.hasPotionEffect(PotionEffectType.SLOW)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 1, false, false));
        }
    }
}
