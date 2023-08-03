package in.prismar.game.item.impl.fishing;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.fishing.FishingReward;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.spigot.item.ItemUtil;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

@Setter
public class FishingRodItem extends CustomItem {

    private int minWaitTimeSeconds = 1;
    private int maxWaitTimeSeconds = 2;

    public FishingRodItem(String id, String displayName) {
        super(id, Material.FISHING_ROD, displayName);
        allFlags();
    }

    @CustomItemEvent
    public void onCall(Player player, Game game, CustomItemHolder holder, PlayerFishEvent event) {
        event.getHook().setMinWaitTime(minWaitTimeSeconds * 20);
        event.getHook().setMaxWaitTime(maxWaitTimeSeconds * 20);
        if( event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if(event.getCaught() != null) {
                event.getCaught().remove();
            }
            event.setExpToDrop(0);
            FishingReward reward = game.getFishingRewardRegistry().getRandomByAssignedRod(getId());
            ItemStack stack = reward.getItem().getItem().clone();
            ItemUtil.giveItem(player, stack);
            if(stack.hasItemMeta()) {
                if(stack.getItemMeta().hasDisplayName()) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§7You have caught §a" + stack.getItemMeta().getDisplayName());
                }
            }
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1f);
        }
    }
}
