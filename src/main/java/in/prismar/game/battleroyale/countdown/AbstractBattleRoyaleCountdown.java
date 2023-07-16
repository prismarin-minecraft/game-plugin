package in.prismar.game.battleroyale.countdown;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@Getter
public abstract class AbstractBattleRoyaleCountdown implements BattleRoyaleCountdown, Runnable{

    protected final BattleRoyaleService service;
    protected final BattleRoyaleGame game;
    protected BukkitTask task;
    private final int maxSeconds;
    protected int currentSeconds;

    public AbstractBattleRoyaleCountdown(BattleRoyaleService service, BattleRoyaleGame game, int maxSeconds) {
        this.service = service;
        this.game = game;
        this.maxSeconds = maxSeconds;
        this.currentSeconds = maxSeconds;
    }

    protected void reset() {
        this.currentSeconds = this.maxSeconds;
    }

    @Override
    public void start() {
        onStart();
        task = Bukkit.getScheduler().runTaskTimer(service.getGame(), this, 20, 20);
    }

    @Override
    public void stop() {
        if(task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public boolean isRunning() {
        return task != null;
    }

    @Override
    public void run() {
        if(currentSeconds <= 0) {
            onEnd();
            stop();
            return;
        }
        onUpdate();
        currentSeconds--;
    }
}
