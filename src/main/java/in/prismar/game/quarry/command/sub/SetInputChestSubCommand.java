package in.prismar.game.quarry.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.quarry.QuarryService;
import in.prismar.game.quarry.model.Quarry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

public class SetInputChestSubCommand extends HelpSubCommand<Player> {

    private final QuarryService service;

    public SetInputChestSubCommand(QuarryService service) {
        super("setinputchest");
        this.service = service;

        setUsage("<id>");
        setDescription("Set the input chest");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1).toLowerCase();
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis quarry does not exists");
                return true;
            }
            RayTraceResult result = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getLocation().getDirection(), 5);
            if(result.getHitBlock() == null) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease look at a chest");
                return true;
            }
            if(result.getHitBlock().getType() != Material.CHEST) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease look at a chest");
                return true;
            }
            final Quarry quarry = service.getRepository().findById(id);
            quarry.setInputLocation(result.getHitBlock().getLocation());
            service.getRepository().save(quarry);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have set the input chest for the quarry §e" + id);
            return true;
        }
        return false;
    }
}
