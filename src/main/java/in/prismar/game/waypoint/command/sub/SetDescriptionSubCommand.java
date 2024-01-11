package in.prismar.game.waypoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.game.waypoint.model.Waypoint;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class SetDescriptionSubCommand extends HelpSubCommand<Player> {

    private final WaypointService service;

    public SetDescriptionSubCommand(WaypointService service) {
        super("setdescription");
        this.service = service;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "waypoint.setup");

        setUsage("<id> <description>");
        setDescription("Set a waypoint description");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1).toLowerCase();
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis waypoint does not exists");
                return true;
            }
            final String description = arguments.getCombinedArgsFrom(2);
            Waypoint waypoint = service.getRepository().findById(id);
            waypoint.setDescription(description);
            service.getRepository().save(waypoint);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have updated the waypoint §6" + id);
            return true;
        }
        return false;
    }
}
