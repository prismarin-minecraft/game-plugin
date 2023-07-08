package in.prismar.game.battleroyale.model;

import lombok.Getter;

import java.util.*;

@Getter
public class BattleRoyaleGame {

    private final int playersSize;
    private final int teamSize;

    private List<BattleRoyaleTeam> teams;
    private List<BattleRoyaleQueueEntry> queue;

    public BattleRoyaleGame(int playersSize, int teamSize) {
        this.playersSize = playersSize;
        this.teamSize = teamSize;

        this.teams = new ArrayList<>();
        this.queue = new ArrayList<>();
    }
}
