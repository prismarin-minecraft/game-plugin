package in.prismar.game.item.impl.fishing;

import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.model.CustomItem;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

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

            player.sendMessage("Caught");

        }
    }
}
