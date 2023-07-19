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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class WarmUpCountdown extends AbstractBattleRoyaleCountdown {
    public WarmUpCountdown(BattleRoyaleService service, BattleRoyaleGame game) {
        super(service, game, game.getProperties().getWarmUpTime());
    }

    @Override
    public void onStart() {
        service.executeForAll(game, player -> {
            player.getInventory().clear();
            player.setHealth(20);
            player.setGameMode(GameMode.ADVENTURE);
            player.getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            service.getScoreboardProvider().recreateSidebar(player);
        });
        for(BattleRoyaleTeam team : game.getTeams()) {
            service.randomTeleport(game, team);
        }
    }

    @Override
    public void onUpdate() {
        switch (currentSeconds) {
            case 120: case 100: case 80: case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2: case 1:
                service.executeForAll(game, player -> {
                    player.sendMessage(PrismarinConstants.PREFIX + "§7The warmup phase ends in §2" + TimeUtil.showInMinutesSeconds(currentSeconds));
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.35f, 1f);
                });
                return;
        }
    }

    @Override
    public void onEnd() {
        service.executeForAll(game, player -> {
            player.sendMessage(PrismarinConstants.PREFIX + "§7The §awarmup phase §7has ended. Everyone is now vulnerable");
            player.playSound(player.getLocation(), Sound.ENTITY_SPLASH_POTION_BREAK, 0.35f, 1f);
        });
        service.switchState(game, BattleRoyaleGame.BattleRoyaleGameState.IN_GAME);
    }
}
