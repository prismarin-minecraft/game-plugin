package in.prismar.game.waypoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeleteSubCommand extends HelpSubCommand<Player> {

    private final WaypointService service;

    public DeleteSubCommand(WaypointService service) {
        super("delete");
        this.service = service;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "waypoint.setup");

        setUsage("<id>");
        setDescription("Delete a waypoint");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1).toLowerCase();
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis waypoint does not exists");
                return true;
            }
            service.getRepository().deleteById(id);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have deleted a the waypoint §6" + id);
            return true;
        }
        return false;
    }
}
