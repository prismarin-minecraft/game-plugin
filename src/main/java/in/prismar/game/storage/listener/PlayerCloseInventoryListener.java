package in.prismar.game.storage.listener;

import in.prismar.game.storage.StorageService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

@AutoListener
public class PlayerCloseInventoryListener implements Listener {

    @Inject
    private StorageService service;

    @EventHandler
    public void onCall(InventoryCloseEvent event) {
        if(event.getPlayer() instanceof final Player player) {
            service.closeStorageInventory(player, event.getInventory());
        }
    }
}
