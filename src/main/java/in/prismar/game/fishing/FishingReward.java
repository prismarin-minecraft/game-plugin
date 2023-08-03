package in.prismar.game.fishing;

import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class FishingReward {

    private ItemContainer item;
    private float chance;
    private String assignedRod;

}
