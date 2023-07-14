package in.prismar.game.battleroyale.countdown;

public interface BattleRoyaleCountdown {

    void onStart();
    void onUpdate();
    void onEnd();

    void start();

    void stop();

    boolean isRunning();

    int getCurrentSeconds();

    int getMaxSeconds();
}
