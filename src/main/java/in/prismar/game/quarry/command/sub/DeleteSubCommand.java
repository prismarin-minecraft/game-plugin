package in.prismar.game.quarry.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.quarry.QuarryService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeleteSubCommand extends HelpSubCommand<Player> {

    private final QuarryService service;

    public DeleteSubCommand(QuarryService service) {
        super("delete");
        this.service = service;

        setUsage("<id>");
        setDescription("Delete a quarry");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1).toLowerCase();
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis quarry does not exists");
                return true;
            }
            service.getRepository().deleteById(id);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have deleted a quarry with the id §e" + id);
            return true;
        }
        return false;
    }
}
