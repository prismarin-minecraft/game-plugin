package in.prismar.game.item.impl.melee;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.library.spigot.scheduler.Scheduler;
import in.prismar.library.spigot.scheduler.SchedulerRunnable;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

public class ThrowableTomahawkMeeleItem extends MeleeItem{
    public ThrowableTomahawkMeeleItem() {
        super("throwabletomahawk", Material.IRON_SWORD, "§6Throwable Tomahawk");
        allFlags();
        setUnbreakable(true);

        setCustomModelData(2);
        setAttackSpeed(MeleeAttackSpeed.SLOW);
        setDamage(21);

        addLore(buildDefaultLore().toArray(new String[0]));
    }

    @CustomItemEvent
    public void onInteract(Player player, Game game, CustomItemHolder holder, PlayerInteractEvent event) {
        if(holder.getHoldingType() != CustomItemHoldingType.RIGHT_HAND || event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && player.isSneaking()) {
            player.getInventory().setItemInMainHand(null);
            player.updateInventory();

            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 0.8f, 1f);
            Item item = player.getWorld().dropItem(player.getEyeLocation(), holder.getStack().clone());
            item.setVelocity(player.getLocation().getDirection().multiply(2.5));
            Scheduler.runTimerFor(1, 1, 20 * 8, new SchedulerRunnable() {
                @Override
                public void run() {
                    if(item.isOnGround()) {
                        cancel();
                        return;
                    }
                    for(Entity entity : item.getWorld().getNearbyEntities(item.getLocation(), 1.1, 2, 1.1)) {
                        if(entity instanceof LivingEntity livingEntity) {
                            if(livingEntity.getUniqueId().equals(player.getUniqueId())) {
                                continue;
                            }
                            livingEntity.damage(28, player);
                            player.playSound(player.getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK, 1f, 1.7f);
                            cancel();
                            break;
                        }
                    }
                }
            });
        }

    }
}
