package in.prismar.game.fishing.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.fishing.FishingReward;
import in.prismar.game.fishing.FishingRewardRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.template.Pager;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ListSubCommand extends HelpSubCommand<Player> {

    private final FishingRewardRegistry registry;

    public ListSubCommand(FishingRewardRegistry registry) {
        super("list");
        setDescription("List all rewards");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        Pager pager = new Pager("§dFishing Rewards", 6);
        pager.fill();
        for(FishingReward reward : registry.getEntity()) {
            ItemBuilder builder = new ItemBuilder(reward.getItem().getItem())
                    .addLore("§c ", PrismarinConstants.ARROW_RIGHT + " §7Chance§8: §d" + Math.round(reward.getChance() * 100) + "%", "§c ", "§dLeft click §7to remove");
            pager.addItem(builder.build(), (ClickFrameButtonEvent) (player1, event) -> {
                registry.remove(reward);
                player.performCommand("fishingreward list");
            });
        }
        pager.build();
        pager.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
        return true;
    }
}
