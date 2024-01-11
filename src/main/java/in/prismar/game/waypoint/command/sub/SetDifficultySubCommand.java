package in.prismar.game.waypoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.game.waypoint.model.Waypoint;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SetDifficultySubCommand extends HelpSubCommand<Player> {

    private final WaypointService service;

    public SetDifficultySubCommand(WaypointService service) {
        super("setdifficulty");
        this.service = service;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "waypoint.setup");

        setUsage("<id> <difficulty>");
        setDescription("Set a waypoint difficulty");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1).toLowerCase();
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis waypoint does not exists");
                return true;
            }
            final String difficulty = arguments.getCombinedArgsFrom(2);
            Waypoint waypoint = service.getRepository().findById(id);
            waypoint.setDifficulty(difficulty);
            service.getRepository().save(waypoint);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have updated the waypoint §6" + id);
            return true;
        }
        return false;
    }
}
