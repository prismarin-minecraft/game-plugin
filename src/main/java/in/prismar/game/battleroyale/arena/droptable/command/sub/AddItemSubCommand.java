package in.prismar.game.battleroyale.arena.droptable.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.arena.droptable.DropTableFile;
import in.prismar.game.battleroyale.arena.droptable.DropTableItem;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Player;

public class AddItemSubCommand extends HelpSubCommand<Player> {

    private final DropTableFile dropTable;

    public AddItemSubCommand(DropTableFile dropTable) {
        super("addItem");
        setDescription("Add item to table");
        setUsage("<chanceInPercentage>");
        this.dropTable = dropTable;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            if(!ItemUtil.hasItemInHand(player, true)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold an item in your main hand");
                return true;
            }
            double chance = arguments.getDouble(1) * 100;
            dropTable.register(player.getInventory().getItemInMainHand().clone(), chance);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have successfully added a new §aitem §7to the droptable");
            return true;
        }
        return false;
    }
}
