package in.prismar.game.battleroyale.listener;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

@AutoListener
public class PlayerClickListener implements Listener {

    @Inject
    private BattleRoyaleService service;

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(event.getClickedInventory() != null && event.getCurrentItem() != null) {
            if(event.getCurrentItem().getType() == Material.ELYTRA) {
                Player player = (Player) event.getWhoClicked();
                if(service.getRegistry().isPlaying(player)) {
                    event.setCancelled(true);
                    event.setResult(Event.Result.DENY);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryMoveItemEvent event) {
        if(event.getItem() != null && event.getSource() != null) {
            if(event.getItem().getType() == Material.ELYTRA && event.getSource().getHolder() instanceof Player player) {
                if(service.getRegistry().isPlaying(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
