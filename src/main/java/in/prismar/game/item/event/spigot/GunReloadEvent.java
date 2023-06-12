package in.prismar.game.item.event.spigot;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class GunReloadEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player player;
    private final GunPlayer gunPlayer;
    private final Gun gun;

    @Setter
    private int reloadTimeInTicks;

    @Setter
    private boolean cancelled;

    public GunReloadEvent(Player player, GunPlayer gunPlayer, Gun gun, int reloadTimeInTicks) {
        this.player = player;
        this.gunPlayer = gunPlayer;
        this.gun = gun;
        this.reloadTimeInTicks = reloadTimeInTicks;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}