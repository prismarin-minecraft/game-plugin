package in.prismar.game.warzone.dungeon;

import in.prismar.api.PrismarinApi;
import in.prismar.api.region.RegionProvider;
import in.prismar.api.warzone.dungeon.DungeonInfo;
import in.prismar.api.warzone.dungeon.DungeonProvider;
import in.prismar.game.Game;
import in.prismar.game.warzone.WarzoneService;
import in.prismar.game.warzone.dungeon.task.DungeonScoreboardTask;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Optional;

@Service
@Getter
public class DungeonService implements DungeonProvider {

    @Inject
    private DungeonRegistry registry;

    @Inject
    private WarzoneService warzoneService;

    @Inject
    private Game game;

    private final RegionProvider regionProvider;

    public DungeonService() {
        this.regionProvider = PrismarinApi.getProvider(RegionProvider.class);
    }

    @SafeInitialize
    private void initialize() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(game, new DungeonScoreboardTask(this), 20, 20);
    }

    @Override
    public Optional<DungeonInfo> getDungeonByLocation(Location location) {
        for(Dungeon dungeon : registry.getAll()) {
            if(!warzoneService.isInWarzone(location)) {
                continue;
            }
            if(regionProvider.isIn(location, dungeon.getId().toLowerCase())) {
                return Optional.of(dungeon);
            }
        }
        return Optional.empty();
    }
}
