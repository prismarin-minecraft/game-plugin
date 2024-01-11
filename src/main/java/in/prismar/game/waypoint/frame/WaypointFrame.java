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

import java.util.ArrayList;
import java.util.List;

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

        final String arrow = PrismarinConstants.ARROW_RIGHT + " §7";
        for (Waypoint waypoint : service.getRepository().findAll()) {
            List<String> lore = new ArrayList<>();
            lore.add("§c ");
            if(waypoint.getDescription() != null) {
                lore.add(arrow + "§7Description§8:");
                String[] messages = waypoint.getDescription().split("\n");
                for(String message : messages) {
                    lore.add("   §8- §6" + message.replace("&", "§"));
                }
            }
            if(waypoint.getDifficulty() != null) {
                lore.add(arrow + "§7Difficulty§8: §e" + waypoint.getDifficulty().replace("&", "§"));
            }
            if(waypoint.getLoot() != null) {
                lore.add(arrow + "§7Loot§8: §c" + waypoint.getLoot().replace("&", "§"));
            }
            lore.add("§c ");
            if (current != null) {
                if (waypoint.getId().equals(current.getId())) {
                    addButton(slot, new ItemBuilder(waypoint.getIcon().getItem())
                            .addLore(lore.toArray(new String[0]))
                            .addLore("§c", PrismarinConstants.ARROW_RIGHT + " §aSelected", "§c", "§6Click §7to deselect")
                            .glow().build(), (ClickFrameButtonEvent) (player12, event) -> {
                        service.removeWaypoint(player);
                        WaypointFrame frame = new WaypointFrame(service, player);
                        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
                    });
                    slot++;
                    continue;
                }
            }
            addButton(slot, new ItemBuilder(waypoint.getIcon().getItem()).addLore(lore.toArray(new String[0])).addLore("§c", "§6Click me §7to track").build(), (ClickFrameButtonEvent) (player1, event) -> {
                service.setWaypoint(player, waypoint);
                WaypointFrame frame = new WaypointFrame(service, player);
                frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
            });
            slot++;
        }


        build();
    }
}
