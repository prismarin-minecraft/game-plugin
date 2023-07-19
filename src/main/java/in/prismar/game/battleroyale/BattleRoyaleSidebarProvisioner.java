package in.prismar.game.battleroyale;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.scoreboard.sidebar.SidebarEntity;
import in.prismar.api.scoreboard.sidebar.SidebarProvisioner;
import in.prismar.game.battleroyale.countdown.impl.InGameCountdown;
import in.prismar.game.battleroyale.countdown.impl.WarmUpCountdown;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleParticipant;
import in.prismar.game.battleroyale.model.BattleRoyaleTeam;
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
            entity.addDynamicLine("  §8│ §7", "NBM", team -> {
                if(game.getCountdown() instanceof InGameCountdown countdown) {
                    if(countdown.isFinished()) {
                        team.setSuffix("§cLast stage");
                        return;
                    }
                    if(countdown.isShrinking()) {
                        team.setSuffix("§7Border is shrinking§8: §e" + TimeUtil.showInMinutesSeconds(countdown.getCurrentSeconds()));
                    } else {
                        team.setSuffix("§7Next border move§8: §a" + TimeUtil.showInMinutesSeconds(countdown.getCurrentSeconds()));
                    }
                } else if(game.getCountdown() instanceof WarmUpCountdown countdown) {
                    team.setSuffix("§7Warm up§8: §a" + TimeUtil.showInMinutesSeconds(countdown.getCurrentSeconds()));
                }
            });
            BattleRoyaleTeam battleRoyaleTeam = service.getRegistry().getTeamByPlayer(game, player);
            entity.addStaticLine("§a§c§a ");
            entity.addStaticLine(" §8§aTeam");
            for(BattleRoyaleParticipant participant : battleRoyaleTeam.getParticipants().values()) {
                entity.addDynamicLine("  §8▪ §7"+participant.getPlayer().getName()+"§8: ", participant.getPlayer().getName(), team -> {
                    team.setSuffix(participant.getState().getDisplayName());
                });
            }
            entity.addStaticLine("§2§c ");
            return true;
        }
        return false;
    }
}
