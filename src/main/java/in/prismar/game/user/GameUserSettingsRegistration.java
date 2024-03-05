package in.prismar.game.user;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.settings.UserSettingsProvider;
import in.prismar.game.user.settings.PartyLimitNotificationsUserSetting;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class GameUserSettingsRegistration {

    private UserProvider<User> provider;
    private UserSettingsProvider<User> settingsProvider;

    public GameUserSettingsRegistration() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
        this.settingsProvider = PrismarinApi.getProvider(UserSettingsProvider.class);
        initSettings();
    }

    private void initSettings() {
        settingsProvider.registerSetting(new PartyLimitNotificationsUserSetting());
    }

}
