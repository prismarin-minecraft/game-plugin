package in.prismar.game.battleroyale.arena.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class ListSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleArenaService service;

    public ListSubCommand(BattleRoyaleArenaService service) {
        super("list");
        setDescription("List all arenas");
        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7Arenas§8: §a" + Joiner.on("§8, §a").join(service.getRepository().findAll()));
        return true;
    }
}
