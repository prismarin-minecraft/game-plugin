package in.prismar.game.fishing.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.fishing.FishingRewardRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Player;

public class AddSubCommand extends HelpSubCommand<Player> {

    private final FishingRewardRegistry registry;

    public AddSubCommand(FishingRewardRegistry registry) {
        super("add");
        setUsage("<chanceInPercentage:1-100> <assignedRod>");
        setDescription("Add a reward");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            if(!ItemUtil.hasItemInHand(player, true)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold an item in your main hand");
                return true;
            }
            final int chance = arguments.getInteger(1);
            final String assignedRod = arguments.getString(2).toLowerCase();
            registry.create(player.getInventory().getItemInMainHand().clone(), (float) chance / 100F, assignedRod);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have successfully added a new item to the pool, assigned to rod §a" + assignedRod);
            return true;
        }
        return false;
    }
}
