package in.prismar.game.battleroyale.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueJoinEvent;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueLeaveEvent;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

@AutoListener
public class BattleRoyaleQueueListener implements Listener {

    @Inject
    private BattleRoyaleService service;

    @EventHandler
    public void onJoin(BattleRoyaleQueueJoinEvent event) {
        Player player = event.getPlayer();
        BattleRoyaleGame game = event.getGame();
        final String message = PrismarinConstants.PREFIX + "§a" + player.getName() + " §7joined the BattleRoyale queue §8[§a" + (service.getQueuePlayerCount(game)+1) + "§8/§a" + game.getProperties().getPlayersSize() + "§8]";
        service.sendMessage(event.getGame(), message);
        player.sendMessage(message);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
    }

    @EventHandler
    public void onLeave(BattleRoyaleQueueLeaveEvent event) {
        Player player = event.getPlayer();
        BattleRoyaleGame game = event.getGame();
        final String message = PrismarinConstants.PREFIX + "§c" + player.getName() + " §7left the BattleRoyale queue §8[§c" + (service.getQueuePlayerCount(game)-1) + "§8/§c" + game.getProperties().getPlayersSize() + "§8]";
        service.sendMessage(event.getGame(), message);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1f);
    }
}
