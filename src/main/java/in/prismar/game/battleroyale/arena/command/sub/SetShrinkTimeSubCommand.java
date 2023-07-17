package in.prismar.game.battleroyale.arena.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class SetShrinkTimeSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleArenaService service;

    public SetShrinkTimeSubCommand(BattleRoyaleArenaService service) {
        super("setshrinktime");
        setDescription("Set shrink time of arena");
        setUsage("<id> <shrinkTimeInSeconds>");
        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis arena does not exists");
                return true;
            }
            final int shrinkTime = arguments.getInteger(2);
            BattleRoyaleArena arena = service.getRepository().findById(id);
            arena.setShrinkTime(shrinkTime);
            service.getRepository().save(arena);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have set the shrink time of arena §a" + id);
            return true;
        }
        return false;
    }
}
