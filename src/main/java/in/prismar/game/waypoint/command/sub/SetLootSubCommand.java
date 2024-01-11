package in.prismar.game.waypoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.game.waypoint.model.Waypoint;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class SetLootSubCommand extends HelpSubCommand<Player> {

    private final WaypointService service;

    public SetLootSubCommand(WaypointService service) {
        super("setloot");
        this.service = service;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "waypoint.setup");

        setUsage("<id> <loot>");
        setDescription("Set a waypoint loot");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1).toLowerCase();
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis waypoint does not exists");
                return true;
            }
            final String loot = arguments.getCombinedArgsFrom(2);
            Waypoint waypoint = service.getRepository().findById(id);
            waypoint.setLoot(loot);
            service.getRepository().save(waypoint);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have updated the waypoint §6" + id);
            return true;
        }
        return false;
    }
}
