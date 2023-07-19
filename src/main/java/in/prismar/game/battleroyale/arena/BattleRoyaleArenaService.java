package in.prismar.game.battleroyale.arena;

import in.prismar.game.Game;
import in.prismar.game.battleroyale.arena.droptable.DropTableFile;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.game.battleroyale.arena.repository.BattleRoyaleArenaRepository;
import in.prismar.game.battleroyale.arena.repository.FileBattleRoyaleArenaRepository;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;

@Service
@Getter
public class BattleRoyaleArenaService {

    private BattleRoyaleArenaRepository repository;
    private DropTableFile droptable;

    public BattleRoyaleArenaService(Game game) {
        final String directory = game.getDefaultDirectory().concat("battleroyale").concat(File.separator);
        this.repository = new FileBattleRoyaleArenaRepository(directory);
        this.droptable = new DropTableFile(directory);
    }

    public BattleRoyaleArena create(String id, String displayName) {
        BattleRoyaleArena arena = new BattleRoyaleArena();
        arena.setId(id);
        arena.setDisplayName(displayName);
        arena.setSize(2000);
        arena.setCenter(Bukkit.getWorlds().get(0).getSpawnLocation());
        arena.setDrops(new HashSet<>());
        return repository.create(arena);
    }

    public int getBorderDistance(BattleRoyaleArena arena, Player player) {
        WorldBorder border = arena.getCenter().getWorld().getWorldBorder();
        Location center = border.getCenter();
        double distance = border.getSize() - player.getLocation().distance(center);
        return (int)distance;
    }

    public void prepare(BattleRoyaleArena arena) {
        spawnDrops(arena);
        setBorder(arena, arena.getSize());
    }

    public void cleanDropLocation(Location location) {
        for(Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1, entity -> entity instanceof ItemFrame)) {
            entity.remove();
        }
    }

    public void spawnDrops(BattleRoyaleArena arena) {
        for(Location dropLocation : arena.getDrops()) {
            cleanDropLocation(dropLocation);
            ItemFrame frame = dropLocation.getWorld().spawn(dropLocation, ItemFrame.class);
            frame.setVisible(false);
            frame.setItem(droptable.findRandomItem().clone());
        }
    }


    public void setBorder(BattleRoyaleArena arena, int size) {
        WorldBorder border = arena.getCenter().getWorld().getWorldBorder();
        border.setCenter(arena.getCenter());
        border.setSize(size);
    }

    public int getBorderSize(BattleRoyaleArena arena) {
        WorldBorder border = arena.getCenter().getWorld().getWorldBorder();
        return (int) border.getSize();
    }
}
