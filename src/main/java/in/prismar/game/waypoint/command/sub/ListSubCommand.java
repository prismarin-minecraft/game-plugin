package in.prismar.game.waypoint.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class ListSubCommand extends HelpSubCommand<Player> {

    private final WaypointService service;

    public ListSubCommand(WaypointService service) {
        super("list");
        this.service = service;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "waypoint.setup");

        setDescription("List all waypoints");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7Waypoints§8: §6" + Joiner.on("§8, §6").join(service.getRepository().findAll()));
        return true;
    }
}
