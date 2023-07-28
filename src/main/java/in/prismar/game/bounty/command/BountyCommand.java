package in.prismar.game.bounty.command;

import in.prismar.game.bounty.BountyService;
import in.prismar.game.bounty.command.sub.ListSubCommand;
import in.prismar.game.bounty.command.sub.SetSubCommand;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

@AutoCommand
public class BountyCommand extends HelpCommand<Player> {

    @Inject
    private BountyService service;

    public BountyCommand() {
        super("bounty", "Bounty");
        setAliases("bounties");
        setSenders(Player.class);
        setBaseColor("§c");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new SetSubCommand(service));
        addChild(new ListSubCommand(service));
    }
}
