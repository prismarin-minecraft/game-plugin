package in.prismar.game.battleroyale.countdown.impl;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.countdown.AbstractBattleRoyaleCountdown;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleTeam;
import in.prismar.library.common.time.TimeUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

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
        service.shuffleTeams(game);

        final String arrow = PrismarinConstants.ARROW_RIGHT.concat(" ");
        service.executeForAll(game, player -> {
            player.sendMessage(PrismarinConstants.BORDER);
            player.sendMessage(" ");
            player.sendMessage(arrow + "§a§lBattleRoyale Event");
            player.sendMessage(arrow + "§7has started!");
            player.sendMessage(" ");
            player.sendMessage(arrow + "§7The §awarmup phase §7has started.");
            player.sendMessage(arrow + "§7You are invulnerable while you are in the warmup phase");
            player.sendMessage(" ");
            player.sendMessage(PrismarinConstants.BORDER);
            player.playSound(player.getLocation(), Sound.ITEM_TOTEM_USE, 0.5f, 1f);


            player.getInventory().clear();
            player.setHealth(20);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
        });
        game.getCountdown().stop();
        for(BattleRoyaleTeam team : game.getTeams()) {
            service.randomTeleport(game, team);
        }

        service.switchState(game, BattleRoyaleGame.BattleRoyaleGameState.WARM_UP);
    }
}
