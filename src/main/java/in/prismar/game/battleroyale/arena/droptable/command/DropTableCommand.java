package in.prismar.game.battleroyale.arena.droptable.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.droptable.command.sub.AddItemSubCommand;
import in.prismar.game.battleroyale.arena.droptable.command.sub.ListItemsSubCommand;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

@AutoCommand
public class DropTableCommand extends HelpCommand<Player> {

    @Inject
    private BattleRoyaleArenaService arenaService;

    public DropTableCommand() {
        super("battleroyaledroptable", "BR Droptable");
        setSenders(Player.class);
        setAliases("brdroptable", "brdt");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "battleroyale.droptable.setup");
        setBaseColor("§a");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new AddItemSubCommand(arenaService.getDroptable()));
        addChild(new ListItemsSubCommand(arenaService.getDroptable()));
    }
}
