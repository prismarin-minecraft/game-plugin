package in.prismar.game.item.event.spigot;

import in.prismar.game.item.impl.throwable.ThrowableItem;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
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
public class ThrowableExplodeEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player player;
    private final ThrowableItem item;
    private final Location location;

    @Setter
    private boolean cancelled;

    public ThrowableExplodeEvent(Player player, ThrowableItem item, Location location) {
        this.player = player;
        this.item = item;
        this.location = location;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
