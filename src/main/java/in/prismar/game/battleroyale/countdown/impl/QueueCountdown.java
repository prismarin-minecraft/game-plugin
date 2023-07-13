package in.prismar.game.battleroyale.countdown.impl;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.countdown.AbstractBattleRoyaleCountdown;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;

public class QueueCountdown extends AbstractBattleRoyaleCountdown {
    public QueueCountdown(BattleRoyaleService service, BattleRoyaleGame game) {
        super(service, game, game.getProperties().getQueueTime());
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate() {

    }

    @Override
    public void onEnd() {

    }
}
