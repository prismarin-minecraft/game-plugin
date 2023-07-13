package in.prismar.game.battleroyale.event;

import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleQueueEntry;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class BattleRoyaleQueueLeaveEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final BattleRoyaleGame game;
    private final BattleRoyaleQueueEntry entry;
    private final Player player;

    public BattleRoyaleQueueLeaveEvent(BattleRoyaleGame game, BattleRoyaleQueueEntry entry, Player player) {
        this.game = game;
        this.entry = entry;
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
