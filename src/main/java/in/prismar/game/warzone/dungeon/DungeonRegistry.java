package in.prismar.game.warzone.dungeon;

import in.prismar.api.configuration.node.event.ConfigRefreshEvent;
import in.prismar.game.web.config.file.ConfigNodeFile;
import in.prismar.library.common.event.EventSubscriber;
import in.prismar.library.common.registry.LocalMapRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;

import java.util.List;

@Service
public class DungeonRegistry extends LocalMapRegistry<String, Dungeon> {

    @Inject
    private ConfigNodeFile nodeFile;

    public DungeonRegistry() {
        super(false, false);
    }

    @SafeInitialize
    private void initialize() {
        load();
        nodeFile.getEventBus().subscribe(ConfigRefreshEvent.class, entity -> {
            load();
        });
    }

    public void load() {
        clear();
        register(new Dungeon("Laboratory", nodeFile.getString("Dungeons.Laboratory.Title", "&6Anderson").replace("&", "§"),
                nodeFile.getString("Dungeons.Laboratory.Spawner name", "laboratory").replace("&", "§"),
                nodeFile.getInteger("Dungeons.Laboratory.Duration", 1800000),
                nodeFile.getInteger("Dungeons.Laboratory.Reduce timer", 300000),
                nodeFile.getString("Dungeons.Laboratory.End boss id", "zahar_abomination")));
    }

    public void register(Dungeon dungeon) {
        register(dungeon.getId().toLowerCase(), dungeon);
    }
}
