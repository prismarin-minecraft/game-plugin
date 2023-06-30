package in.prismar.game.listener;

import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoListener
public class CraftItemListener implements Listener {

    @EventHandler
    public void onCall(CraftItemEvent event) {
        final String result = event.getRecipe().getResult().getType().name();
        if (result.contains("LEGGINGS") || result.contains("HELMET")
                || result.contains("CHESTPLATE")
                || result.contains("BOOTS") ||
                result.contains("SWORD")
                || result.contains("BOW")
                || result.contains("ARROW") || result.contains("GRINDSTONE") || result.contains("SHIELD")) {
            event.setCancelled(true);
        }
    }
}
