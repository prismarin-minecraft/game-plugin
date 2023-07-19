package in.prismar.game.battleroyale.arena.listener;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.meta.anno.AutoListener;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

@AutoListener
public class PlayerFrameEntityListener implements Listener {

    @Inject
    private BattleRoyaleArenaService service;

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if(event.getRightClicked() instanceof ItemFrame frame) {
            System.out.println("Yes: " + frame.getLocation().getBlock().getLocation());
            for(BattleRoyaleArena arena : service.getRepository().findAll()) {
                if(frame.getWorld().getName().equals(arena.getCenter().getWorld().getName())) {
                    if(arena.getDrops().contains(frame.getLocation().getBlock().getLocation())) {
                        event.setCancelled(true);
                        frame.remove();

                        Player player = event.getPlayer();
                        ItemStack stack = service.getDroptable().find(frame.getItem());
                        if(stack != null) {
                            ItemUtil.giveItem(player, stack.clone());
                            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.6f, 1f);
                        }
                    }
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof ItemFrame frame) {
            for(BattleRoyaleArena arena : service.getRepository().findAll()) {
                if(frame.getWorld().getName().equals(arena.getCenter().getWorld().getName())) {
                    if(event.getDamager() instanceof Player player) {
                        if(player.getGameMode() == GameMode.CREATIVE && player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "battleroyale.arena.setup")) {
                            return;
                        }
                    }
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }


}
