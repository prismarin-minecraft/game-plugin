package in.prismar.game.battleroyale.arena.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.location.LocationUtil;
import org.bukkit.entity.Player;

public class AddDropLocationSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleArenaService service;

    public AddDropLocationSubCommand(BattleRoyaleArenaService service) {
        super("addDropLocation");
        setDescription("Add drop location");
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
            arena.getDrops().add(LocationUtil.getCenterOfBlock(player.getLocation().getBlock().getLocation()));
            service.getRepository().save(arena);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have added a new drop location for the arena §a" + id);
            return true;
        }
        return false;
    }
}
