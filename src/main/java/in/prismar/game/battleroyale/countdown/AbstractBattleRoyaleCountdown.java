package in.prismar.game.battleroyale.countdown;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import lombok.Getter;

@Getter
public abstract class AbstractBattleRoyaleCountdown implements BattleRoyaleCountdown {

    protected final BattleRoyaleService service;
    protected final BattleRoyaleGame game;
    private final int maxSeconds;
    private int currentSeconds;

    public AbstractBattleRoyaleCountdown(BattleRoyaleService service, BattleRoyaleGame game, int maxSeconds) {
        this.service = service;
        this.game = game;
        this.maxSeconds = maxSeconds;
        this.currentSeconds = maxSeconds;
    }
}
