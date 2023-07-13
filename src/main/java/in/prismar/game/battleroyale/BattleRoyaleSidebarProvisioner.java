package in.prismar.game.battleroyale;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.scoreboard.sidebar.SidebarEntity;
import in.prismar.api.scoreboard.sidebar.SidebarProvisioner;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
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
            entity.addStaticLine("§a§c ");
            entity.addStaticLine("§a§lBattleRoyale");
            entity.addStaticLine("§2§c ");
            return true;
        }
        return false;
    }
}
