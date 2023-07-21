package in.prismar.game.battleroyale.command;

import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.command.sub.CreateSubCommand;
import in.prismar.game.battleroyale.command.sub.QueueSubCommand;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;


@AutoCommand
public class BattleRoyaleCommand extends HelpCommand<Player> {

    @Inject
    private BattleRoyaleService service;

    public BattleRoyaleCommand() {
        super("battleroyale", "BattleRoyale");
        setAliases("broyale");
        setBaseColor("§a");
        setSenders(Player.class);
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(service));
        addChild(new QueueSubCommand(service));
    }
}
