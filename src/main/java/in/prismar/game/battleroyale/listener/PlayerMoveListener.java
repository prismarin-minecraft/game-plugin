package in.prismar.game.battleroyale.listener;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

@AutoListener
public class PlayerMoveListener implements Listener {

    @Inject
    private BattleRoyaleService service;

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(player.isOnGround()) {
            ItemStack stack = player.getInventory().getChestplate();
            if(stack != null) {
                if(stack.getType() == Material.ELYTRA) {
                    if(service.getRegistry().isInGame(player)) {
                       player.getInventory().setChestplate(null);
                       player.updateInventory();
                    }
                }
            }
        }
    }


}
