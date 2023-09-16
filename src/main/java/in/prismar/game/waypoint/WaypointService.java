package in.prismar.game.waypoint;

import in.prismar.api.compass.CompassProvider;
import in.prismar.api.meta.Provider;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.Game;
import in.prismar.game.waypoint.model.Waypoint;
import in.prismar.game.waypoint.repository.FileWaypointRepository;
import in.prismar.game.waypoint.repository.WaypointRepository;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

@Service
@Getter
public class WaypointService {

    private static final String TAG = "waypoint";

    private WaypointRepository repository;

    private final Game game;

    @Provider
    private UserProvider<User> userProvider;

    @Provider
    private CompassProvider compassProvider;

    public WaypointService(Game game) {
        this.game = game;
        this.repository = new FileWaypointRepository(game.getDefaultDirectory());
    }

    public Waypoint create(String id, Location location, ItemStack icon) {
        Waypoint waypoint = new Waypoint();
        waypoint.setId(id);
        waypoint.setIcon(new ItemContainer(icon));
        waypoint.setLocation(location);
        return repository.create(waypoint);
    }

    public void setWaypoint(Player player, Waypoint waypoint) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        user.setTag(TAG, waypoint);
        refreshWaypoint(player, waypoint);
    }

    public void removeWaypoint(Player player) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        Waypoint waypoint = user.getTag(TAG);
        user.removeTag(TAG);
        refreshWaypoint(player, waypoint);
    }

    public void refreshWaypoint(Player player, Waypoint waypoint) {
        if(!hasWaypoint(player)) {
            compassProvider.removeEntry(player, waypoint.getIcon().getItem().getItemMeta().getDisplayName());
            return;
        }
        if(game.getWarzoneService().isInWarzone(player)) {
            compassProvider.addEntry(player, waypoint.getLocation(), waypoint.getIcon().getItem().getItemMeta().getDisplayName(), "GOLD");
        } else {
            compassProvider.removeEntry(player, waypoint.getIcon().getItem().getItemMeta().getDisplayName());
        }
    }

    @Nullable
    public Waypoint getWaypoint(Player player) {
        User user = userProvider.getUserByUUID(player.getUniqueId());
        if(user.containsTag(TAG)) {
            return user.getTag(TAG);
        }
        return null;
    }

    public boolean hasWaypoint(Player player) {
        return getWaypoint(player) != null;
    }
}
