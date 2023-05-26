package in.prismar.game.perk;

import in.prismar.api.PrismarinConstants;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
public enum Perk {

    ESCAPE(new ItemBuilder(Material.IRON_DOOR).setName("§6Escape")
            .addLore(PrismarinConstants.BORDER)
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §aPros§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §7When you get hit you get speed for §a2 §7seconds")
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §cCons§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §7There is a §c8 §7seconds §7cooldown between hits")
            .addLore("§7")
            .addLore(PrismarinConstants.BORDER)
            .allFlags()
            .build(), 0, "§f七七七七七七七七败"),
    EXPERT(new ItemBuilder(Material.STICK).setName("§6Expert")
            .setCustomModelData(2)
            .addLore(PrismarinConstants.BORDER)
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §aPros§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §7You can carry §a2 §7lethals")
            .addLore("§7")
            .addLore(PrismarinConstants.BORDER)
            .allFlags()
            .build(), 150000, "§f七七七七七七七七敗"),
    DEADEYE(new ItemBuilder(Material.SPYGLASS).setName("§6Deadeye")
            .addLore(PrismarinConstants.BORDER)
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §aPros§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §7When you get hit, the enemy will be")
            .addLore(PrismarinConstants.LISTING_DOT + " §7marked for §a2 §7seconds")
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §cCons§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §7There is a §c4 §7seconds §7cooldown between hits")
            .addLore("§7")
            .addLore(PrismarinConstants.BORDER)
            .allFlags()
            .build(), 150000, "§f七七七七七七七七折"),
    FASTHANDS(new ItemBuilder(Material.BEACON).setName("§6§lFast Hands")
            .addLore(PrismarinConstants.BORDER)
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §aPros§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §7Faster §areloading")
            .addLore(PrismarinConstants.LISTING_DOT + " §7Speed §a1")
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §cCons§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §c-20% §7Protection")
            .addLore("§7")
            .addLore(PrismarinConstants.BORDER)
            .allFlags()
            .build(), 0, "§f七七七七七七七七象"),
    FORTIFY(new ItemBuilder(Material.NETHERITE_CHESTPLATE).setName("§6§lFortify")
            .addLore(PrismarinConstants.BORDER)
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §aPros§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §a+20% §7Protection")
            .addLore("§7")
            .addLore(PrismarinConstants.ARROW_RIGHT + " §cCons§8:")
            .addLore(PrismarinConstants.LISTING_DOT + " §7Slowness §c2")
            .addLore("§7")
            .addLore(PrismarinConstants.BORDER)
            .allFlags()
            .build(), 0, "§f七七七七七七七七子");

    private final ItemStack icon;
    private final double buyPrice;
    private final String title;
}
