package in.prismar.game.waypoint.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.waypoint.WaypointService;
import in.prismar.game.waypoint.command.sub.CreateSubCommand;
import in.prismar.game.waypoint.command.sub.DeleteSubCommand;
import in.prismar.game.waypoint.command.sub.ListSubCommand;
import in.prismar.game.waypoint.frame.WaypointFrame;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoCommand
public class WaypointCommand extends HelpCommand<Player> {

    @Inject
    private WaypointService service;

    public WaypointCommand() {
        super("waypoint", "Waypoint");
        setSenders(Player.class);
        setBaseColor("§6");

        setAliases("wp", "waypoints", "compass");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(service));
        addChild(new DeleteSubCommand(service));
        addChild(new ListSubCommand(service));
    }

    @Override
    public boolean raw(Player player, SpigotArguments arguments) {
        if(arguments.getLength() == 0) {
            if(!service.getGame().getWarzoneService().isInWarzone(player)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou can only execute this command inside warzone");
                return false;
            }
            WaypointFrame frame = new WaypointFrame(service, player);
            frame.openInventory(player, Sound.BLOCK_IRON_DOOR_OPEN, 0.5f);
            return false;
        }
        return true;
    }
}
