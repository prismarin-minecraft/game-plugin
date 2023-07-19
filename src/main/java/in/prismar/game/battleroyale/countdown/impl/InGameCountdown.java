package in.prismar.game.battleroyale.countdown.impl;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.countdown.AbstractBattleRoyaleCountdown;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.library.common.time.TimeUtil;
import lombok.Getter;
import org.bukkit.Sound;

@Getter
public class InGameCountdown extends AbstractBattleRoyaleCountdown {

    private boolean shrinking;
    private boolean finished;
    private int shrinkingAmount;

    public InGameCountdown(BattleRoyaleService service, BattleRoyaleGame game) {
        super(service, game, game.getProperties().getShrinkTime());
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate() {
       // System.out.println("Secs: " + currentSeconds + " Shrinking: " + shrinking + " / Finished: " + finished);
        if(finished) {
            return;
        }
        if(shrinking) {
            if(shrinkingAmount <= 0) {
                shrinking = false;
                currentSeconds = getMaxSeconds();
                service.executeForAll(game, player -> {
                    player.sendMessage(PrismarinConstants.PREFIX + "§7The §aborder §7has stopped");
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0.35f, 1f);
                });
                return;
            }
            final int borderSize = service.getArenaService().getBorderSize(game.getArena());
            if(borderSize <= game.getProperties().getMaxShrinkAmount()) {
                service.getArenaService().setBorder(game.getArena(), game.getProperties().getMaxShrinkAmount());
                finished = true;
                service.executeForAll(game, player -> {
                    player.sendMessage(PrismarinConstants.PREFIX + "§7The §aborder §7has stopped");
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0.35f, 1f);
                });
                return;
            }
            if(currentSeconds <= 2) {
                currentSeconds = 2;
            }
            final int steps = game.getProperties().getShrinkAmount() / game.getProperties().getShrinkProgressTime();
            shrinkingAmount -= steps;
            service.getArenaService().setBorder(game.getArena(), borderSize - steps);
            return;
        }
        switch (currentSeconds) {
            case 120: case 100: case 80: case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2:
                service.executeForAll(game, player -> {
                    player.sendMessage(PrismarinConstants.PREFIX + "§7The border will shrink in §2" + TimeUtil.showInMinutesSeconds(currentSeconds));
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST , 0.35f, 2f);
                });
                break;
            case 1:
                service.executeForAll(game, player -> {
                    player.sendMessage(PrismarinConstants.PREFIX + "§7The §aborder §7is now shrinking");
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.35f, 1f);
                });
                shrinking = true;
                shrinkingAmount = game.getProperties().getShrinkAmount();
                currentSeconds = game.getProperties().getShrinkProgressTime();
                break;
        }
    }

    @Override
    public void onEnd() {

    }
}
