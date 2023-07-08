package in.prismar.game.battleroyale.arena.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class DeleteSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleArenaService service;

    public DeleteSubCommand(BattleRoyaleArenaService service) {
        super("delete");
        setDescription("Delete arena");
        setUsage("<id>");
        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis arena does not exists");
                return true;
            }
            BattleRoyaleArena arena = service.getRepository().findById(id);
            service.getRepository().delete(arena);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have delete the arena §a" + id);
            return true;
        }
        return false;
    }
}
