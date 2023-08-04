package in.prismar.game.item.impl.fishing;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.farm.FishingProvider;
import in.prismar.game.Game;
import in.prismar.game.item.event.CustomItemEvent;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.model.CustomItem;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;

@Setter
public class FishingRodItem extends CustomItem {

    private FishingProvider fishingProvider;

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
            if(this.fishingProvider == null) {
                this.fishingProvider = PrismarinApi.getProvider(FishingProvider.class);
            }
            String name = fishingProvider.giveRewardByAssignedRod(player, getId());
            if(name != null) {
                player.sendMessage(PrismarinConstants.PREFIX + "§7You have fished §a" + name);
            }
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1f);
        }
    }
}
