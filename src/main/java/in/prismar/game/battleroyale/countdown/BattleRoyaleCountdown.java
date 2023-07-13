package in.prismar.game.battleroyale.countdown;

public interface BattleRoyaleCountdown {

    void onStart();
    void onUpdate();
    void onEnd();

    int getCurrentSeconds();

    int getMaxSeconds();
}
