package in.prismar.game.user.settings;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.settings.UserSetting;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class PartyLimitNotificationsUserSetting implements UserSetting<User> {

    private final ItemStack icon;

    public PartyLimitNotificationsUserSetting() {
        this.icon = new ItemBuilder(Material.PURPLE_DYE).setName("§dParty limit notifications").build();
    }

    @Override
    public void onChange(User user, boolean state) {

    }

    @Override
    public boolean getDefaultValue(User user) {
        return true;
    }

    @Override
    public ItemStack getIcon(User user) {
        return this.icon;
    }


    @Override
    public String getKey() {
        return "partylimitnotify";
    }

    @Override
    public String getPermission() {
        return PrismarinConstants.PERMISSION_PREFIX + "party.limit.notify";
    }
}
