package in.prismar.game.battleroyale.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.battleroyale.BattleRoyaleService;
import in.prismar.game.battleroyale.frame.BattleRoyaleQueueFrame;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleQueueEntry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.text.InteractiveTextBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Optional;

public class QueueSubCommand extends HelpSubCommand<Player> {

    private final BattleRoyaleService service;

    public QueueSubCommand(BattleRoyaleService service) {
        super("queue");
        setAliases("q");
        setDescription("<id>");
        setDescription("Join the battleroyale queue");

        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {

        if(arguments.getLength() >= 2) {
            final String sub = arguments.getString(1);
            if(arguments.getLength() == 3) {
                if(sub.equalsIgnoreCase("join")) {
                    final String id = arguments.getString(2);
                    BattleRoyaleGame game = service.getRegistry().getById(id);
                    if(game == null) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cA game with this id does not exists");
                        return true;
                    }
                    if(game.getState() != BattleRoyaleGame.BattleRoyaleGameState.QUEUE) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou cannot join this game");
                        return true;
                    }
                    if(service.getRegistry().isInGame(player)) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already playing");
                        return true;
                    }
                    service.addToQueue(game, player);
                    return true;
                } else if(sub.equalsIgnoreCase("invite")) {
                    BattleRoyaleGame current = service.getRegistry().getByPlayer(player);
                    if(current == null) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not in a battleroyale game");
                        return true;
                    }
                    if(current.getState() != BattleRoyaleGame.BattleRoyaleGameState.QUEUE) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not in a battleroyale queue");
                        return true;
                    }
                    BattleRoyaleQueueEntry entry = service.getRegistry().getQueueEntryByPlayer(current, player);
                    if(entry.getPlayers().size() >= current.getProperties().getTeamSize()) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYour queue is already full");
                        return true;
                    }
                    Player target = arguments.getOnlinePlayer(2);
                    BattleRoyaleGame game = service.getRegistry().getByPlayer(target);
                    if(game != null) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cThis player is already in a battleroyale game or in queue");
                        return true;
                    }
                    service.sendQueueMessage(entry, PrismarinConstants.PREFIX + "§a" + player.getName() + " §7invited §a" + target.getName() + " §7to the battleroyale queue");
                    entry.getInvites().add(target.getUniqueId());
                    target.sendMessage(PrismarinConstants.PREFIX + "§7You were invited by §a" + player.getName() + " §7to join their battleroyale queue");
                    InteractiveTextBuilder builder = new InteractiveTextBuilder(PrismarinConstants.PREFIX);
                    builder.addText("§8[§a§lAccept§8]", "/broyale queue accept " + player.getName(), "§aClick §7to join the queue");
                    builder.addText(" §8| ");
                    builder.addText("§8[§c§lDeny§8]", "/broyale queue deny " + player.getName(), "§aClick §7to deny this invitation");
                    builder.send(target);
                } else if(sub.equalsIgnoreCase("accept")) {
                    if(service.getRegistry().isInGame(player)) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already in game or in a queue");
                        return true;
                    }
                    Player target = arguments.getOnlinePlayer(2);
                    BattleRoyaleGame game = service.getRegistry().getByPlayer(target);
                    if(game == null) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cThis player is not in a queue");
                        return true;
                    }
                    if(game.getState() != BattleRoyaleGame.BattleRoyaleGameState.QUEUE) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cThis player game is not in a queue state");
                        return true;
                    }
                    BattleRoyaleQueueEntry entry = service.getRegistry().getQueueEntryByPlayer(game, target);
                    if(!entry.getInvites().contains(player.getUniqueId())) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou were not invited to this queue");
                        return true;
                    }
                    if(entry.getPlayers().size() >= game.getProperties().getTeamSize()) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cThis queue is already full");
                        return true;
                    }
                    entry.getInvites().remove(player.getUniqueId());
                    service.sendQueueMessage(entry, PrismarinConstants.PREFIX + "§a" + player.getName() + " §7joined your battleroyale queue");
                    service.addToSpecificQueue(game, entry, player);
                } else if(sub.equalsIgnoreCase("deny")) {
                    if(service.getRegistry().isInGame(player)) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already in game or in a queue");
                        return true;
                    }
                    Player target = arguments.getOnlinePlayer(2);
                    BattleRoyaleGame game = service.getRegistry().getByPlayer(target);
                    if(game == null) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cThis player is not in a queue");
                        return true;
                    }
                    if(game.getState() != BattleRoyaleGame.BattleRoyaleGameState.QUEUE) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cThis player game is not in a queue state");
                        return true;
                    }
                    BattleRoyaleQueueEntry entry = service.getRegistry().getQueueEntryByPlayer(game, target);
                    if(!entry.getInvites().contains(player.getUniqueId())) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou were not invited to this queue");
                        return true;
                    }
                    entry.getInvites().remove(player.getUniqueId());
                    service.sendQueueMessage(entry, PrismarinConstants.PREFIX + "§c" + player.getName() + " §7denied the battleroyale queue invitation");
                    target.sendMessage(PrismarinConstants.PREFIX + "§7You have denied a battleroyale queue invitation");
                }
            } else if(arguments.getLength() == 2) {
                if(sub.equalsIgnoreCase("leave")) {
                    BattleRoyaleGame game = service.getRegistry().getByPlayer(player);
                    if(game == null) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not in the battleroyale queue");
                        return true;
                    }
                    BattleRoyaleQueueEntry entry = service.removeFromQueue(player);
                    if(entry == null) {
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not in the battleroyale queue");
                        return true;
                    }
                    service.sendQueueMessage(entry, PrismarinConstants.PREFIX + "§c" + player.getName() + " §7left your battleroyale queue");
                }
            }
        }
        BattleRoyaleGame currentGame = service.getRegistry().getByPlayer(player);
        if(currentGame != null) {
            if(currentGame.getState() == BattleRoyaleGame.BattleRoyaleGameState.QUEUE) {
                BattleRoyaleQueueEntry entry = service.getRegistry().getQueueEntryByPlayer(currentGame, player);
                BattleRoyaleQueueFrame frame = new BattleRoyaleQueueFrame(service, currentGame, entry);
                frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
                return true;
            }
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already in game");
            return true;
        }
        Optional<BattleRoyaleGame> optional = service.getRegistry().findGameByState(BattleRoyaleGame.BattleRoyaleGameState.QUEUE);
        if(optional.isEmpty()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cThere is currently no battleroyale game running");
            return true;
        }
        service.addToQueue(optional.get(), player);
        return false;
    }
}
