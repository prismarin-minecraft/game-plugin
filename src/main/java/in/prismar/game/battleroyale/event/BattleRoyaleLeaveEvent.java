package in.prismar.game.battleroyale.event;

import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleParticipant;
import in.prismar.game.battleroyale.model.BattleRoyaleQueueEntry;
import in.prismar.game.battleroyale.model.BattleRoyaleTeam;
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
public class BattleRoyaleLeaveEvent extends Event {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    private final BattleRoyaleGame game;
    private final BattleRoyaleTeam team;
    private final BattleRoyaleParticipant participant;
    private final boolean force;

    public BattleRoyaleLeaveEvent(BattleRoyaleGame game, BattleRoyaleTeam team, BattleRoyaleParticipant participant, boolean force) {
        this.game = game;
        this.team = team;
        this.participant = participant;
        this.force = force;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
