package in.prismar.game.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

@AutoListener
public class ServerListPingListener implements Listener {

    @EventHandler
    public void onCall(ServerListPingEvent event) {
        event.setMotd("§b§lPrismarin Season " + PrismarinConstants.CURRENT_SEASON + "\n§8[§cGuns §8- §c §4Mobs §8- §6Clans §8- §3Warzone§8]");
    }
}
