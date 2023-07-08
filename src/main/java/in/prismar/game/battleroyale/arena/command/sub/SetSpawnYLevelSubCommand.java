package in.prismar.game.battleroyale.arena.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class SetSpawnYLevelSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleArenaService service;

    public SetSpawnYLevelSubCommand(BattleRoyaleArenaService service) {
        super("setspawnylevel");
        setDescription("Set spawn y level of arena");
        setUsage("<id> <y>");
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
            final int level = arguments.getInteger(2);
            BattleRoyaleArena arena = service.getRepository().findById(id);
            arena.setSpawnYLevel(level);
            service.getRepository().save(arena);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have set the spawn y level of arena §a" + id);
            return true;
        }
        return false;
    }
}
