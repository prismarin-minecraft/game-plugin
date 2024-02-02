package in.prismar.game.kit.frame;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.kit.KitService;
import in.prismar.game.kit.model.Kit;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Comparator;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class KitFrame extends Frame {

    private static final int[] SLOTS = {13, 20, 30, 32, 24};

    private final KitService service;

    public KitFrame(KitService service, Player player) {
        super("§aKits", 5);
        this.service = service;
        fill(Material.BLACK_STAINED_GLASS_PANE);

        UserProvider<User> userProvider = PrismarinApi.getProvider(UserProvider.class);
        User user = userProvider.getUserByUUID(player.getUniqueId());

        int index = 0;
        for(Kit kit : service.getRepository().findAll().stream().filter(kit -> !kit.isRespawn() && !kit.isStarter()).sorted((o1, o2) -> Integer.compare(o2.getWeight(), o1.getWeight())).toList()) {
            ItemBuilder builder = new ItemBuilder(kit.getIcon().getItem()).allFlags();
            builder.addLore("§c ");
            if(user.isTimestampAvailable("kit." + kit.getId()) || player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "kit.bypass")) {
                if(player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "kit." + kit.getId())) {
                    builder.addLore("§aLeft click §7to redeem this kit");
                }
            } else {
                long time = user.getTimestamp("kit." + kit.getId()) - System.currentTimeMillis();
                builder.addLore(PrismarinConstants.ARROW_RIGHT + " §7Cooldown§8: §c" + TimeUtil.convertToThreeDigits(time / 1000));
                builder.addLore("§c");
            }
            builder.addLore("§aRight click §7to preview this kit");
            addButton(SLOTS[index], builder.build(), (ClickFrameButtonEvent) (player1, event) -> {
                if(event.isRightClick()) {
                    PreviewKitFrame frame = new PreviewKitFrame(service, kit);
                    frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
                    return;
                }
                if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "kit." + kit.getId())) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cYou do not have permission to claim this kit");
                    return;
                }
                if(!user.isTimestampAvailable("kit." + kit.getId()) && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "kit.bypass")) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cThis kit is on cooldown");
                    return;
                }
                for(ItemStack stack : kit.getItems().getItem()) {
                    if(stack == null) {
                        continue;
                    }
                    if(stack.getType() == Material.AIR) {
                        continue;
                    }
                    ItemUtil.giveItem(player, stack.clone());
                }
                user.setTimestamp("kit." + kit.getId(), System.currentTimeMillis() + 1000 * kit.getCooldownInSeconds());
                userProvider.saveAsync(user, true);
                player.sendMessage(PrismarinConstants.PREFIX + "§7You have claimed the kit " + kit.getIcon().getItem().getItemMeta().getDisplayName());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1f);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
            });
            index++;
        }


        build();
    }
}
