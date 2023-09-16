package in.prismar.game.waypoint.frame;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.game.waypoint.model.Waypoint;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class WaypointFrame extends Frame {

    private final WaypointService service;
    private final Player player;

    public WaypointFrame(WaypointService service, Player player) {
        super("§6Waypoints", 2);
        this.service = service;
        this.player = player;

        fill();

        int slot = 0;
        Waypoint current = service.getWaypoint(player);
        for (Waypoint waypoint : service.getRepository().findAll()) {
            if (current != null) {
                if (waypoint.getId().equals(current.getId())) {
                    addButton(slot, new ItemBuilder(waypoint.getIcon().getItem())
                            .addLore("§c ", PrismarinConstants.ARROW_RIGHT + " §aSelected", "§c", "§6Click §7to deselect")
                            .glow().build(), (ClickFrameButtonEvent) (player12, event) -> {
                        service.removeWaypoint(player);
                        WaypointFrame frame = new WaypointFrame(service, player);
                        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
                    });
                    slot++;
                    continue;
                }
            }
            addButton(slot, new ItemBuilder(waypoint.getIcon().getItem()).addLore("§c", "§6Click me §7to track").build(), (ClickFrameButtonEvent) (player1, event) -> {
                service.setWaypoint(player, waypoint);
                WaypointFrame frame = new WaypointFrame(service, player);
                frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
            });
            slot++;
        }


        build();
    }
}
