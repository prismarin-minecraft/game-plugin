package in.prismar.game.battleroyale.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.game.battleroyale.frame.BattleRoyaleQueueFrame;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleQueueEntry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.text.InteractiveTextBuilder;
import lombok.SneakyThrows;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Optional;

public class CreateSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleService service;

    public CreateSubCommand(BattleRoyaleService service) {
        super("create");
        setAliases("q");
        setDescription("<arena> <props>");
        setDescription("Create a new battleroyale event");
        this.service = service;
    }

    @SneakyThrows
    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String arenaId = arguments.getString(1);
            if(!service.getArenaService().getRepository().existsById(arenaId)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis arena does not exists");
                return true;
            }
            BattleRoyaleArena arena = service.getArenaService().getRepository().findById(arenaId);
            final String props = arguments.getCombinedArgsFrom(2);
            BattleRoyaleGame game;
            try {
                game = service.create(props, arena);
            }catch (Exception exception) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThere was an error while trying to create a new battleroyale event");
                return true;
            }
            final String arrow = PrismarinConstants.ARROW_RIGHT.concat(" ");
            player.sendMessage(PrismarinConstants.BORDER);
            player.sendMessage(" ");
            player.sendMessage(arrow + "§7You have created a new §aBattleRoyale Event");
            player.sendMessage(arrow + "§7Properties§8:");
            for(Field field : game.getProperties().getClass().getDeclaredFields()) {
                if(!field.canAccess(game.getProperties())) {
                    field.setAccessible(true);
                }
                player.sendMessage(PrismarinConstants.LISTING_DOT + " §7" + capitalize(field.getName()) + "§8: §a" + field.get(game.getProperties()));
            }
            player.sendMessage(" ");
            player.sendMessage(PrismarinConstants.BORDER);
            return true;
        }
        return false;
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
