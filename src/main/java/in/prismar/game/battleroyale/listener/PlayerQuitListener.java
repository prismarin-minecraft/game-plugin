package in.prismar.game.battleroyale.listener;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

@AutoListener
public class PlayerQuitListener implements Listener {

    @Inject
    private BattleRoyaleService service;

    @EventHandler
    public void onMove(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if(service.removeFromQueue(player) == null) {
            service.removeFromGame(player, true);
        }
    }


}
