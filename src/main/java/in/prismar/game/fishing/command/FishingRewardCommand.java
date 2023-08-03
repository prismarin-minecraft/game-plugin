package in.prismar.game.fishing.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.fishing.FishingRewardRegistry;
import in.prismar.game.fishing.command.sub.AddSubCommand;
import in.prismar.game.fishing.command.sub.ListSubCommand;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

@AutoCommand
public class FishingRewardCommand extends HelpCommand<Player> {

    @Inject
    private FishingRewardRegistry registry;

    public FishingRewardCommand() {
        super("fishingreward", "Fishing Rewards");
        setAliases("fishingrewards");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "fishing.reward.admin");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new AddSubCommand(registry));
        addChild(new ListSubCommand(registry));
    }
}
