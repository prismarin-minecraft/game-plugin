package in.prismar.game.quarry.listener;

import in.prismar.game.quarry.QuarryService;
import in.prismar.game.quarry.model.Quarry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


@AutoListener
public class PlayerInteractListener implements Listener {

    @Inject
    private QuarryService service;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if(block.getType() == Material.CHEST) {
                event.setCancelled(true);
                for(Quarry quarry : service.getRepository().findAll()) {
                    if(quarry.getInputLocation().equals(block.getLocation())) {
                        service.openInput(player, quarry);
                        return;
                    }
                    if(quarry.getOutputLocation().equals(block.getLocation())) {
                        service.openOutput(player, quarry);
                        return;
                    }
                }
            }
        }

    }
}
