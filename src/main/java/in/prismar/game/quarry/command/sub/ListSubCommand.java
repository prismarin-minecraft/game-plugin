package in.prismar.game.quarry.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.quarry.QuarryService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class ListSubCommand extends HelpSubCommand<Player> {

    private final QuarryService service;

    public ListSubCommand(QuarryService service) {
        super("list");
        this.service = service;

        setDescription("List all quarries");

        setPermission(PrismarinConstants.PERMISSION_PREFIX + "quarry.setup");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7Quarries§8: §e" + Joiner.on("§8, §e").join(service.getRepository().findAll()));
        return true;
    }
}
