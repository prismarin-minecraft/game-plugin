package in.prismar.game.bundle.frame;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BundleFrame extends Frame {

    private static final int[] SLOTS = {10, 12, 14, 16};

    private final BundleFacade facade;
    private final User user;


    public BundleFrame(BundleFacade facade, User user) {
        super("§aBundles", 3);
        this.facade = facade;
        this.user = user;
        if(user.getSeasonData().getAttachments() == null) {
            user.getSeasonData().setAttachments(new HashMap<>());
        }
        fill();

        int index = 0;
        for(Bundle bundle : facade.getRepository().findAll()) {
            if(bundle.isSeasonal()) {
                int current = (int) user.getSeasonData().getAttachments().getOrDefault("bundles." + bundle.getId(), 0);
                ItemBuilder builder = new ItemBuilder(bundle.getIcon().getItem())
                        .addLore("§c")
                        .addLore("§7Currently in possession§8: §b" + current)
                        .addLore("§c").allFlags();
                if(current > 0) {
                    builder.addLore("§7Click me to redeem");
                }
                addButton(SLOTS[index], builder.build(), (ClickFrameButtonEvent) (player, event) -> {
                    int amount = (int) user.getSeasonData().getAttachments().getOrDefault("bundles." + bundle.getId(), 0);
                    if(amount == 0) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou don't have any bundles of this type in possession.");
                        return;
                    }
                    amount--;
                    user.getSeasonData().getAttachments().put("bundles." + bundle.getId(), amount);
                    UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
                    provider.saveAsync(user, true);
                    player.closeInventory();
                    player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 0.8f, 1);
                    for(ItemStack stack : bundle.getContainer().getItem()) {
                        if(stack != null) {
                            if(stack.getType() != Material.AIR) {
                                ItemUtil.giveItem(player, stack);
                            }
                        }
                    }
                });
                index++;
            }
        }



        build();
    }
}
