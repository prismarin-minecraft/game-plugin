package in.prismar.game.waypoint.listener;

import in.prismar.game.waypoint.WaypointService;
import in.prismar.game.waypoint.model.Waypoint;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

@AutoListener
public class PlayerWorldChangeListener implements Listener {

    @Inject
    private WaypointService service;

    @EventHandler
    public void onCall(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        Waypoint waypoint = service.getWaypoint(player);
        if(waypoint == null) {
            return;
        }
        service.refreshWaypoint(player, waypoint);
    }
}
