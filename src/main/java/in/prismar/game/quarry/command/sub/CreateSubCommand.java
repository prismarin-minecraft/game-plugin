package in.prismar.game.quarry.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.quarry.QuarryService;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CreateSubCommand extends HelpSubCommand<Player> {

    private final QuarryService service;

    public CreateSubCommand(QuarryService service) {
        super("create");
        this.service = service;

        setUsage("<id> <producePerSecond> <displayName...>");
        setDescription("Create a new quarry");

        setPermission(PrismarinConstants.PERMISSION_PREFIX + "quarry.setup");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 4) {
            final String id = arguments.getString(1).toLowerCase();
            if(service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis quarry already exists");
                return true;
            }
            if(!ItemUtil.hasItemInHand(player, true)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold your desired output item in your hand");
                return true;
            }
            final ItemStack output = player.getInventory().getItemInMainHand().clone();
            final int producePerSecond = arguments.getInteger(2);
            final String displayName = arguments.getCombinedArgsFrom(3).replace("&", "§");
            service.create(id, output, producePerSecond, displayName);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully created a new quarry with the id §e" + id);
            return true;
        }
        return false;
    }
}
