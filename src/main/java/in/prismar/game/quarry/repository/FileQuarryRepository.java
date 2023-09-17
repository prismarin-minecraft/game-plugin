package in.prismar.game.quarry.repository;

import com.google.gson.GsonBuilder;
import in.prismar.game.quarry.model.Quarry;
import in.prismar.library.file.gson.GsonRepository;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;

import java.io.File;

public class FileQuarryRepository extends GsonRepository<Quarry> implements QuarryRepository{
    public FileQuarryRepository(String directory) {
        super(directory.concat("quarries/").concat(File.separator), Quarry.class, "Quarry", 10000);
        loadAll();
        for(Quarry quarry : findAll()) {
            quarry.getOutput().deserialize();
        }
    }

    @Override
    public void interceptEntry(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
