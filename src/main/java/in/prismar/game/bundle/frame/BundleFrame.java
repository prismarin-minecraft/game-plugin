package in.prismar.game.bundle.frame;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.common.math.NumberFormatter;
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

    private static final int[] SLOTS = {11, 13, 14, 15, 17};

    private final BundleFacade facade;
    private final User user;

    private final Player player;


    public BundleFrame(BundleFacade facade, User user, Player player) {
        super("§aBundles", 3);
        this.player = player;
        this.facade = facade;
        this.user = user;
        if(user.getSeasonData().getAttachments() == null) {
            user.getSeasonData().setAttachments(new HashMap<>());
        }
        fill();

        int index = 0;
        for(Bundle bundle : facade.getRepository().findAll()) {
            if(bundle.isSeasonal()) {
                ItemBuilder builder = new ItemBuilder(bundle.getIcon().getItem())
                        .addLore("§c").allFlags();
                if(player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "bundles." + bundle.getId())) {
                    if(!user.getSeasonData().getAttachments().containsKey("bundles." + bundle.getId())) {
                        builder.addLore("§7Click me to redeem");
                    } else {
                        builder.addLore("§cYou already redeemed this bundle");
                    }
                } else {
                    builder.addLore("§cYou do not own this bundle");
                }
                addButton(SLOTS[index], builder.build(), (ClickFrameButtonEvent) (player1, event) -> {
                    if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "bundles." + bundle.getId())) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou don't have any bundles of this type in possession.");
                        return;
                    }
                    if(user.getSeasonData().getAttachments().containsKey("bundles." + bundle.getId())) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou already redeemed this bundle.");
                        return;
                    }
                    user.getSeasonData().getAttachments().put("bundles." + bundle.getId(), true);
                    if(bundle.getBalance() > 0) {
                        user.getSeasonData().setBalance(user.getSeasonData().getBalance() + bundle.getBalance());
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1);
                        player.sendMessage(PrismarinConstants.PREFIX + "§7You received §a" +
                                NumberFormatter.formatDoubleToThousands(bundle.getBalance()) + " $ §7for redeeming the bundle " + bundle.getDisplayName());
                    }
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
