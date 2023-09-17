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

public class SetProducePerSecondSubCommand extends HelpSubCommand<Player> {

    private final QuarryService service;

    public SetProducePerSecondSubCommand(QuarryService service) {
        super("setproducepersecond");
        this.service = service;

        setUsage("<id> <producePerSecond>");
        setDescription("Update produce per second");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1).toLowerCase();
            if(!service.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis quarry does not exists");
                return true;
            }
            final int producePerSec = arguments.getInteger(2);
            final Quarry quarry = service.getRepository().findById(id);
            quarry.setProducePerSecond(producePerSec);
            service.getRepository().save(quarry);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have set the produce per second value for the quarry §e" + id);
            return true;
        }
        return false;
    }
}
