package in.prismar.game.battleroyale.arena.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.command.sub.*;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

@AutoCommand
public class ArenaCommand extends HelpCommand<Player> {

    @Inject
    private BattleRoyaleArenaService arenaService;

    public ArenaCommand() {
        super("battleroyalearena", "BR Arena");
        setSenders(Player.class);
        setAliases("brarena", "bra");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "battleroyale.arena.setup");
        setBaseColor("§a");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(arenaService));
        addChild(new DeleteSubCommand(arenaService));
        addChild(new ListSubCommand(arenaService));
        addChild(new SetCenterSubCommand(arenaService));
        addChild(new SetSpawnYLevelSubCommand(arenaService));
        addChild(new AddDropLocationSubCommand(arenaService));
        addChild(new ListDropLocationsSubCommand(arenaService));
    }
}
