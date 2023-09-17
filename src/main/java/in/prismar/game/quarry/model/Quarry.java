package in.prismar.game.quarry.model;

import in.prismar.game.quarry.frame.QuarryInputFrame;
import in.prismar.game.quarry.frame.QuarryOutputFrame;
import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class Quarry extends StringRepositoryEntity {

    private String displayName;
    private ItemContainer output;

    private Location inputLocation;
    private Location outputLocation;

    private int inputAmount;
    private int outputAmount;
    private int producePerSecond;

    private transient int currentFuelConsumptionSeconds;

    private transient QuarryInputFrame inputFrame;
    private transient QuarryOutputFrame outputFrame;



    @Override
    public String toString() {
        return getId();
    }
}
