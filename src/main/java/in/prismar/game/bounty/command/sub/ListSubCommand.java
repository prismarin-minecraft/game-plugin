package in.prismar.game.bounty.command.sub;

import in.prismar.game.bounty.BountyService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

public class ListSubCommand extends HelpSubCommand<Player> {

    private final BountyService service;

    public ListSubCommand(BountyService service) {
        super("list");
        this.service = service;
        setDescription("View all bounties");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.performCommand("warp bounty");
        return true;
    }
}
