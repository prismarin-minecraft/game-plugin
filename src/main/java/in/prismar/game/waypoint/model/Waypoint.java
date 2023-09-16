package in.prismar.game.waypoint.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class Waypoint extends StringRepositoryEntity {

    private ItemContainer icon;
    private Location location;

    @Override
    public String toString() {
        return getId();
    }
}
