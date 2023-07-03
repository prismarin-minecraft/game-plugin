package in.prismar.game.interactable.event;

import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.interactable.model.keylock.KeyLock;
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
public class InteractableKeyLockEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final Player player;
    private final KeyLock keyLock;
    private final boolean pre;

    @Setter
    private boolean cancelled;

    public InteractableKeyLockEvent(Player player, KeyLock keyLock, boolean pre) {
        this.player = player;
        this.keyLock = keyLock;
        this.pre = pre;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
