package in.prismar.game.kit.listener;

import in.prismar.game.kit.KitService;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

@AutoListener
public class PlayerRespawnListener implements Listener {

    @Inject
    private WarzoneService warzoneService;

    @Inject
    private KitService kitService;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(warzoneService.isInWarzone(event.getRespawnLocation())) {
            kitService.giveRespawnKit(player);
        }
    }
}
