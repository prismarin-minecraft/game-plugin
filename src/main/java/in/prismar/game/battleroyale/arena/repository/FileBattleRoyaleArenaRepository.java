package in.prismar.game.battleroyale.arena.repository;

import com.google.gson.GsonBuilder;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.file.gson.GsonCompactRepository;
import in.prismar.library.file.gson.GsonRepository;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;

import java.io.File;

public class FileBattleRoyaleArenaRepository extends GsonRepository<BattleRoyaleArena> implements BattleRoyaleArenaRepository {
    public FileBattleRoyaleArenaRepository(String directory) {
        super(directory.concat("arenas" + File.separator), BattleRoyaleArena.class, "BattleRoyaleArena", 10000);
        loadAll();
    }

    @Override
    public void interceptEntry(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
