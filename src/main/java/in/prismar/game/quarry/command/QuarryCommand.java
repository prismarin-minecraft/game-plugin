package in.prismar.game.quarry.command;

import in.prismar.game.quarry.QuarryService;
import in.prismar.game.quarry.command.sub.*;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

@AutoCommand
public class QuarryCommand extends HelpCommand<Player> {

    @Inject
    private QuarryService service;

    public QuarryCommand() {
        super("quarry", "Quarry");
        setSenders(Player.class);
        setBaseColor("§e");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(service));
        addChild(new DeleteSubCommand(service));
        addChild(new ListSubCommand(service));
        addChild(new SetInputChestSubCommand(service));
        addChild(new SetOutputChestSubCommand(service));
        addChild(new SetProducePerSecondSubCommand(service));
    }
}
