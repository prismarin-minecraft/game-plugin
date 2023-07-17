package in.prismar.game.battleroyale;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.scoreboard.sidebar.SidebarEntity;
import in.prismar.api.scoreboard.sidebar.SidebarProvisioner;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.common.time.TimeUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class BattleRoyaleSidebarProvisioner implements SidebarProvisioner {

    private final BattleRoyaleService service;

    @Override
    public boolean provide(SidebarEntity entity) {
        Player player = entity.getPlayer();
        BattleRoyaleGame game = service.getRegistry().getByPlayer(player);
        if(game != null) {
            if(game.getState() == BattleRoyaleGame.BattleRoyaleGameState.QUEUE) {
                return false;
            }
            entity.addStaticLine("§a§c ");
            entity.addStaticLine(" §8§aGame");
            entity.addDynamicLine("  §8│ §7Alive§8: ", "Alive", team -> {
                team.setSuffix("§a" + game.getAliveCount());
            });
            entity.addDynamicLine("  §8│ §7Distance to border§8: ", "DTB", team -> {
                team.setSuffix("§a" + service.getArenaService().getBorderDistance(game.getArena(), player) + "m");
            });
            entity.addDynamicLine("  §8│ §7Next border move§8: ", "NBM", team -> {
                long difference = game.getNextBorderMove() - System.currentTimeMillis();
                team.setSuffix("§a" + TimeUtil.showInMinutesSeconds(difference / 1000));
            });

            entity.addStaticLine("§a§c§a ");
            entity.addStaticLine(" §8§aTeam");
            entity.addStaticLine("§2§c ");
            return true;
        }
        return false;
    }
}
