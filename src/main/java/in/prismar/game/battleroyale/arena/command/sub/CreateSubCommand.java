package in.prismar.game.battleroyale.arena.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class CreateSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleArenaService service;

    public CreateSubCommand(BattleRoyaleArenaService service) {
        super("create");
        setDescription("Create a new arena");
        setUsage("<id> <display>");
        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis arena already exists");
                return true;
            }
            final String displayName = arguments.getCombinedArgsFrom(2);
            service.create(id, displayName);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have successfully created the arena §a" + id);
            return true;
        }
        return false;
    }
}
