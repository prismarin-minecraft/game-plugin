package in.prismar.game.battleroyale.arena.droptable.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.droptable.DropTableFile;
import in.prismar.game.battleroyale.arena.droptable.DropTableItem;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.template.Pager;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ListItemsSubCommand extends HelpSubCommand<Player> {

    private final DropTableFile dropTable;

    public ListItemsSubCommand(DropTableFile dropTable) {
        super("listItems");
        setDescription("List all items in table");
        this.dropTable = dropTable;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        Pager pager = new Pager("§aBR Droptable", 6);
        pager.fill();
        for(DropTableItem item : dropTable.getEntity()) {
            pager.addItem(new ItemBuilder(item.getItem().getItem())
                    .addLore("§c ", PrismarinConstants.ARROW_RIGHT + " §7Chance§8: §a" + Math.round(item.getChance()), "§c ", "§aLeft click §7to remove")
                    .build(), (ClickFrameButtonEvent) (player1, event) -> {
                dropTable.getEntity().remove(item);
                dropTable.save();
                player.performCommand("brdt listItems");
            });
        }
        pager.build();
        pager.openInventory(player, Sound.UI_BUTTON_CLICK, 0.45f);
        return true;
    }
}
