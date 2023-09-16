package in.prismar.game.waypoint.repository;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import in.prismar.game.waypoint.model.Waypoint;
import in.prismar.library.file.gson.GsonCompactRepository;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;

import java.util.Map;

public class FileWaypointRepository extends GsonCompactRepository<Waypoint> implements WaypointRepository {
    public FileWaypointRepository(String directory) {
        super(directory.concat("waypoints.json"), new TypeToken<Map<String, Waypoint>>(){});
        for(Waypoint waypoint : getEntity().values()) {
            waypoint.getIcon().deserialize();
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
