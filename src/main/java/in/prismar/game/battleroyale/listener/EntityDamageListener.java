package in.prismar.game.battleroyale.listener;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import javax.inject.Inject;

@AutoListener
public class EntityDamageListener implements Listener {

    @Inject
    private BattleRoyaleService service;

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FALL && event.getEntity() instanceof Player player) {
            BattleRoyaleGame game = service.getRegistry().getByPlayer(player);
            if(game == null) {
                return;
            }
            if(game.getState() == BattleRoyaleGame.BattleRoyaleGameState.QUEUE) {
                return;
            }
            if(game.getState() == BattleRoyaleGame.BattleRoyaleGameState.ENDING || game.getState() == BattleRoyaleGame.BattleRoyaleGameState.WARM_UP) {
                event.setCancelled(true);
            }
        }
    }


}
