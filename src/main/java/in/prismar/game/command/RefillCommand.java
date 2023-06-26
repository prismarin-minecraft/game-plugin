package in.prismar.game.command;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.common.time.TimeUtil;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class RefillCommand extends PlayerCommand {

    @Inject
    private CustomItemRegistry itemRegistry;

    private final UserProvider<User> userProvider;
    private final ConfigStore configStore;

    public RefillCommand() {
        super("refill");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "refill");

        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(!ItemUtil.hasItemInHandAndHasDisplayName(player, true)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold a gun in your main hand");
            return true;
        }
        if(!user.isTimestampAvailable("refill") && !player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "refill.bypass")) {
            long until = user.getTimestamp("refill");
            long remaining = (until - System.currentTimeMillis())/1000;
            player.sendMessage(PrismarinConstants.PREFIX + "§7You refill is on cooldown §8(§c" + TimeUtil.convertToThreeDigits(remaining) + "§8)");
            return true;
        }
        if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "refill.bypass")) {
            final long cooldown = Long.parseLong(configStore.getProperty("refill.cooldown"));
            user.setTimestamp("refill", System.currentTimeMillis() + cooldown);
        }
        final ItemStack stack = player.getInventory().getItemInMainHand();
        CustomItem customItem = itemRegistry.getItemByStack(stack);
        if(customItem == null) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cThis item is not a gun");
            return true;
        }
        if(!(customItem instanceof Gun)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cThis item is not a gun");
            return true;
        }
        Gun gun = (Gun)customItem;
        AmmoType ammoType = gun.getAmmoType();
        for (int i = 0; i < ammoType.getRefillStacks(); i++) {
            ItemStack ammo = ammoType.getItem().clone();
            ammo.setAmount(64);
            ItemUtil.giveItem(player, ammo);
        }
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have received §b" + ammoType.getRefillStacks() + " §7stacks of " + ammoType.getItem().getItemMeta().getDisplayName());
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GOLD, 0.6f, 1f);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.4f, 1f);
        return false;
    }
}
