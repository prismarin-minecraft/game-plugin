package in.prismar.game.battleroyale.arena.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

import java.util.List;
import java.util.Set;

@Getter @Setter
public class BattleRoyaleArena extends StringRepositoryEntity {

    private String displayName;
    private int spawnYLevel;
    private int size;
    private Location center;
    private Set<Location> drops;

    @Override
    public String toString() {
        return getId();
    }
}
