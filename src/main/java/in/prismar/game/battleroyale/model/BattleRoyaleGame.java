package in.prismar.game.battleroyale.model;

import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.game.battleroyale.countdown.BattleRoyaleCountdown;
import in.prismar.game.battleroyale.countdown.impl.QueueCountdown;
import in.prismar.library.common.random.RandomStringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class BattleRoyaleGame {

    private final String id;
    private final BattleRoyaleProperties properties;
    private final BattleRoyaleArena arena;

    @Setter
    private BattleRoyaleGameState state;
    @Setter
    private BattleRoyaleCountdown countdown;


    private List<BattleRoyaleTeam> teams;
    private List<BattleRoyaleQueueEntry> queue;

    public BattleRoyaleGame(BattleRoyaleProperties properties, BattleRoyaleArena arena) {
        this.id = RandomStringUtil.generateString(10, false, false);
        this.properties = properties;
        this.arena = arena;
        this.teams = new ArrayList<>();
        this.queue = new ArrayList<>();
    }

    public enum BattleRoyaleGameState {

        QUEUE, WARM_UP, IN_GAME, ENDING
    }
}
