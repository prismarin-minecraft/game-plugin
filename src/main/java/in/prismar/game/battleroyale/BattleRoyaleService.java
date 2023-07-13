package in.prismar.game.battleroyale;

import in.prismar.api.PrismarinApi;
import in.prismar.api.scoreboard.ScoreboardProvider;
import in.prismar.game.battleroyale.arena.BattleRoyaleArenaService;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueJoinEvent;
import in.prismar.game.battleroyale.event.BattleRoyaleQueueLeaveEvent;
import in.prismar.game.battleroyale.model.BattleRoyaleGame;
import in.prismar.game.battleroyale.model.BattleRoyaleParticipant;
import in.prismar.game.battleroyale.model.BattleRoyaleQueueEntry;
import in.prismar.game.battleroyale.model.BattleRoyaleTeam;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Service
@Getter
public class BattleRoyaleService {

    @Inject
    private BattleRoyaleArenaService arenaService;

    @Inject
    private BattleRoyaleRegistry registry;

    private final ScoreboardProvider scoreboardProvider;

    public BattleRoyaleService() {
        this.scoreboardProvider = PrismarinApi.getProvider(ScoreboardProvider.class);
        this.scoreboardProvider.addSidebarProvisioner(new BattleRoyaleSidebarProvisioner(this));
    }

    public BattleRoyaleQueueEntry addToQueue(BattleRoyaleGame game, Player player) {
        BattleRoyaleQueueEntry entry = new BattleRoyaleQueueEntry();
        entry.getPlayers().put(player.getUniqueId(), player);
        Bukkit.getPluginManager().callEvent(new BattleRoyaleQueueJoinEvent(game, entry, player));
        game.getQueue().add(entry);
        return entry;
    }

    public void addToSpecificQueue(BattleRoyaleGame game, BattleRoyaleQueueEntry entry, Player player) {
        entry.getPlayers().put(player.getUniqueId(), player);
        Bukkit.getPluginManager().callEvent(new BattleRoyaleQueueJoinEvent(game, entry, player));
    }

    public int getQueuePlayerCount(BattleRoyaleGame game) {
        int count = 0;
        for(BattleRoyaleQueueEntry entry : game.getQueue()) {
            count += entry.getPlayers().size();
        }
        return count;
    }

    public void sendQueueMessage(BattleRoyaleQueueEntry entry, String message) {
        for(Player player : entry.getPlayers().values()) {
            player.sendMessage(message);
        }
    }

    public BattleRoyaleQueueEntry removeFromQueue(Player player) {
        BattleRoyaleGame game = getRegistry().getByPlayer(player);
        if(game != null) {
            BattleRoyaleQueueEntry entry = registry.getQueueEntryByPlayer(game, player);
            if(entry != null) {
                Bukkit.getPluginManager().callEvent(new BattleRoyaleQueueLeaveEvent(game, entry, player));
                entry.getPlayers().remove(player.getUniqueId());
                if(entry.getPlayers().isEmpty()) {
                    game.getQueue().remove(entry);
                    return entry;
                }
            }
        }
        return null;
    }

    public void executeForAll(BattleRoyaleGame game, Consumer<Player> consumer) {
        for(BattleRoyaleQueueEntry entry : game.getQueue()) {
            for(Player player : entry.getPlayers().values()) {
                consumer.accept(player);
            }
        }
        for(BattleRoyaleTeam team : game.getTeams()) {
            for(BattleRoyaleParticipant participant : team.getParticipants().values()) {
                consumer.accept(participant.getPlayer());
            }
        }
    }

    public void sendMessage(BattleRoyaleGame game, String message) {
        executeForAll(game, player -> player.sendMessage(message));
    }
}
