package in.prismar.game.battleroyale.countdown.impl;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.countdown.AbstractBattleRoyaleCountdown;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.library.common.time.TimeUtil;
import org.bukkit.Sound;

public class QueueCountdown extends AbstractBattleRoyaleCountdown {
    public QueueCountdown(BattleRoyaleService service, BattleRoyaleGame game) {
        super(service, game, game.getProperties().getQueueTime());
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate() {
        if(currentSeconds % 100 == 0) {
            service.announceBattleRoyale(game);
            return;
        }
        switch (currentSeconds) {
            case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2: case 1:
                service.executeForAll(game, player -> {
                    player.sendMessage(PrismarinConstants.PREFIX + "§7The battleroyale event starts in §2" + TimeUtil.showInMinutesSeconds(currentSeconds));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.35f, 1f);
                });
                return;
        }
    }

    @Override
    public void onEnd() {
        final String arrow = PrismarinConstants.ARROW_RIGHT.concat(" ");
        service.executeForAll(game, player -> {
            player.sendMessage(PrismarinConstants.BORDER);
            player.sendMessage(" ");
            player.sendMessage(arrow + "§a§lBattleRoyale Event");
            player.sendMessage(arrow + "§7has started!");
            player.sendMessage(" ");
            player.sendMessage(PrismarinConstants.BORDER);
            player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.5f, 1f);
        });
        //TODO: Teleport
        game.getCountdown().stop();
    }
}
