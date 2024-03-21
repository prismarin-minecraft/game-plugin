package in.prismar.game.resourcepack;

import in.prismar.api.PrismarinConstants;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@AutoCommand
public class ResourcepackRefreshCommand extends SpigotCommand<CommandSender> {

    @Inject
    private ResourcepackService service;

    public ResourcepackRefreshCommand() {
        super("resourcepackrefresh");
        setAliases("rprefresh");
        setSenders(Player.class, ConsoleCommandSender.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "resourcepack.refresh");
    }


    @Override
    public boolean send(CommandSender sender, SpigotArguments arguments) throws CommandException {
        service.load();
        sender.sendMessage(PrismarinConstants.PREFIX + "§7Successfully refreshed the resourcepack §aurl §7and §ahash");
        return true;
    }
}
