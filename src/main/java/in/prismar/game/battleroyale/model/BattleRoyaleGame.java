package in.prismar.game.battleroyale.model;

import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.common.random.RandomStringUtil;
import lombok.Getter;

import java.util.*;

@Getter
public class BattleRoyaleGame {

    private final String id;
    private final BattleRoyaleProperties properties;
    private final BattleRoyaleArena arena;

    private BattleRoyaleGameState state;


    private List<BattleRoyaleTeam> teams;
    private List<BattleRoyaleQueueEntry> queue;

    public BattleRoyaleGame(BattleRoyaleProperties properties, BattleRoyaleArena arena) {
        this.id = RandomStringUtil.generateString(10, false, false);
        this.properties = properties;
        this.arena = arena;
        this.state = BattleRoyaleGameState.QUEUE;

        this.teams = new ArrayList<>();
        this.queue = new ArrayList<>();
    }

    public enum BattleRoyaleGameState {

        QUEUE, WARM_UP, IN_GAME, ENDING
    }
}
