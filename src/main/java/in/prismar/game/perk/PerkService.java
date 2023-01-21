package in.prismar.game.perk;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.bus.GunReloadEvent;
import in.prismar.game.perk.listener.GunReloadListener;
import in.prismar.game.perk.task.PerkTask;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class PerkService  {

    private Game game;

    @Getter
    private final UserProvider<User> userProvider;

    public PerkService(Game game) {
        this.game = game;
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);

        Bukkit.getScheduler().runTaskTimer(game, new PerkTask(this), 20, 20);
    }

    @SafeInitialize
    private void initialize() {
        game.getItemRegistry().getEventBus().subscribe(GunReloadEvent.class, new GunReloadListener(this));
    }

    @Nullable
    public Perk getPerk(Player player) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(user.getSeasonData().getPerk() != null) {
            return Perk.valueOf(user.getSeasonData().getPerk());
        }
        return null;
    }

    public void giveDefaultPerk(Player player) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(user.getSeasonData().getPerk() == null) {
            user.getSeasonData().setPerk(Perk.ESCAPE.name());
            userProvider.saveAsync(user, true);
        }
    }

    public void setPerk(Player player, Perk perk) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.getSeasonData().setPerk(perk.name());
        userProvider.saveAsync(user, true);
    }

    public boolean ownPerk(Player player, Perk perk) {
        return player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "perks." + perk.name().toLowerCase());
    }

    public boolean hasPerkAndAllowedToUse(Player player, Perk perk) {
        if(game.isCurrentlyPlayingAnyMode(player)) {
            if(hasPerk(player, perk)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPerk(Player player, Perk perk) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(user.getSeasonData().getPerk() != null) {
            if(user.getSeasonData().getPerk().equals(perk.name())) {
                return true;
            }
        }
        return false;
    }

}
