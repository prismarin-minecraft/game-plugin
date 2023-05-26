package in.prismar.game.tracer.frame;

import in.prismar.api.user.User;
import in.prismar.game.tracer.BulletTracer;
import in.prismar.game.tracer.BulletTracerRegistry;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.units.qual.A;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BulletTraceFrame extends Frame {

    private static final ItemStack DEFAULT = new ItemBuilder(Material.IRON_BARS).setName("§7Default").allFlags().build();
    private static final ItemStack AIR = new ItemStack(Material.AIR);

    private static final int[] SLOTS = {
             4, 5, 6, 7,
             13, 14, 15, 16,
             22, 23, 24, 25
    };

    private final BulletTracerRegistry registry;
    private final User user;

    public BulletTraceFrame(BulletTracerRegistry registry, Player player, User user) {
        super("§f七七七七七七七七见", 3);
        this.registry = registry;
        this.user = user;


        addButton(0, new ItemBuilder(Material.MAP).setCustomModelData(105).allFlags().setName("§cBack to loadout").build(), (ClickFrameButtonEvent) (player12, event) -> player12.performCommand("loadout"));

        BulletTracer tracer = registry.getByUser(user);
        if (tracer == null) {
            addButton(11, DEFAULT);
        } else {
            addButton(11, new ItemBuilder(tracer.getIcon()).addLore("§c").addLore("§7Click me to unequip this tracer").build(), (ClickFrameButtonEvent) (player1, event) -> {
                registry.removeTracer(user);
                reopen(player);
            });
        }

        int index = 0;


        for(Map.Entry<String, BulletTracer> entry : registry.getLocal().entrySet()) {
            if(registry.hasTracer(player, entry.getKey())) {
                ItemStack icon = new ItemBuilder(entry.getValue().getIcon()).addLore("§c", "§7Click me to equip this tracer").build();
                addButton(SLOTS[index], icon, (ClickFrameButtonEvent) (player1, event) -> {
                    registry.setTracer(user, entry.getKey());
                    reopen(player);
                });
                index++;
            }
        }

        for (int i = index; i < SLOTS.length; i++) {
            addButton(SLOTS[i], AIR);
        }

        build();
    }

    private void reopen(Player player) {
        BulletTraceFrame frame = new BulletTraceFrame(registry, player, user);
        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.6f);
    }
}
