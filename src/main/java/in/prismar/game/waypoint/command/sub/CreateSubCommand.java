package in.prismar.game.waypoint.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreateSubCommand extends HelpSubCommand<Player> {

    private final WaypointService service;

    public CreateSubCommand(WaypointService service) {
        super("create");
        this.service = service;
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "waypoint.setup");

        setUsage("<id>");
        setDescription("Create a new waypoint");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1).toLowerCase();
            if(service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis waypoint already exists");
                return true;
            }
            if(!ItemUtil.hasItemInHandAndHasDisplayName(player, true)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold an item in your hand");
                return true;
            }
            final ItemStack icon = player.getInventory().getItemInMainHand().clone();
            service.create(id, player.getLocation(), icon);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully created a new waypoint with the id §6" + id);
            return true;
        }
        return false;
    }
}
