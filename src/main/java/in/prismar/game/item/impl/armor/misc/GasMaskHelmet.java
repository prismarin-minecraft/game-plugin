package in.prismar.game.item.impl.armor.misc;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.item.impl.armor.ArmorItem;
import in.prismar.game.item.impl.armor.ArmorType;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GasMaskHelmet extends ArmorItem {
    public GasMaskHelmet() {
        super("GasMask", Material.BOWL, "§2Gas Mask", ArmorType.HELMET);
        setHeadProtection(25);
        setBodyProtection(0);
        setCustomModelData(4);
        setCustom(true);
        generateDefaultLore();
        addLore(PrismarinConstants.ARROW_RIGHT + " §7Durability§8: §2600§8/§2600");
        addLore(PrismarinConstants.ARROW_RIGHT + " §7Protects against harmful §2gases");
        addLore("§c ");
    }

    public boolean damage(Game plugin, ItemStack stack, int amount) {
        if (!PersistentItemDataUtil.hasInteger(plugin, stack, "mask_durability")) {
            PersistentItemDataUtil.setInteger(plugin, stack, "mask_durability", 600);
        }
        int durability = PersistentItemDataUtil.getInteger(plugin, stack, "mask_durability");
        int next = durability - amount;
        if (next <= 0) {
            return true;
        }
        PersistentItemDataUtil.setInteger(plugin, stack, "mask_durability", next);
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        int index = findIndex(lore);
        lore.remove(index);
        lore.add(index, PrismarinConstants.ARROW_RIGHT + " §7Durability§8: §2"+next+"/200");
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return false;
    }

    private int findIndex(List<String> lore) {
        for (int i = 0; i < lore.size(); i++) {
            if(lore.get(i).contains("Durability")) {
                return i;
            }
        }
        return -1;
    }

}
