package in.prismar.game.battleroyale;

import in.prismar.game.battleroyale.arena.model.BattleRoyaleArena;
import in.prismar.game.battleroyale.model.*;
import in.prismar.library.meta.anno.Service;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Getter
public class BattleRoyaleRegistry {


    private List<BattleRoyaleGame> games;

    public BattleRoyaleRegistry() {
        this.games = new ArrayList<>();
    }

    public Optional<BattleRoyaleGame> findGameByState(BattleRoyaleGame.BattleRoyaleGameState state) {
        return games.stream().filter(game -> game.getState() == state).findAny();
    }

    public BattleRoyaleGame create(BattleRoyaleProperties properties, BattleRoyaleArena arena) {
        BattleRoyaleGame game = new BattleRoyaleGame(properties, arena);
        this.games.add(game);
        return game;
    }

    public BattleRoyaleParticipant getParticipantByPlayer(Player player) {
        for(BattleRoyaleGame game : this.games) {
            for(BattleRoyaleTeam team : game.getTeams()) {
                if(team.getParticipants().containsKey(player.getUniqueId())) {
                    return team.getParticipants().get(player.getUniqueId());
                }
            }
        }
        return null;
    }

    public BattleRoyaleParticipant getParticipantByPlayer(BattleRoyaleTeam team, Player player) {
        if(team.getParticipants().containsKey(player.getUniqueId())) {
            return team.getParticipants().get(player.getUniqueId());
        }
        return null;
    }

    public BattleRoyaleTeam getTeamByPlayer(Player player) {
        for(BattleRoyaleGame game : this.games) {
            for(BattleRoyaleTeam team : game.getTeams()) {
                if(team.getParticipants().containsKey(player.getUniqueId())) {
                    return team;
                }
            }
        }
        return null;
    }

    public BattleRoyaleTeam getTeamByPlayer(BattleRoyaleGame game, Player player) {
        for(BattleRoyaleTeam team : game.getTeams()) {
            if(team.getParticipants().containsKey(player.getUniqueId())) {
                return team;
            }
        }
        return null;
    }

    public BattleRoyaleGame getByPlayer(Player player) {
        for(BattleRoyaleGame game : this.games) {
            for(BattleRoyaleQueueEntry queueEntry : game.getQueue()) {
                if(queueEntry.getPlayers().containsKey(player.getUniqueId())) {
                    return game;
                }
            }
            for(BattleRoyaleTeam team : game.getTeams()) {
                if(team.getParticipants().containsKey(player.getUniqueId())) {
                    return game;
                }
            }
        }
        return null;
    }

    public BattleRoyaleQueueEntry getQueueEntryByPlayer(BattleRoyaleGame game, Player player) {
        for(BattleRoyaleQueueEntry entry : game.getQueue()) {
            if(entry.getPlayers().containsKey(player.getUniqueId())) {
                return entry;
            }
        }
        return null;
    }

    public boolean isInSameTeam(Player first, Player second) {
        BattleRoyaleTeam team = getTeamByPlayer(first);
        if(team != null) {
            return team.getParticipants().containsKey(second.getUniqueId());
        }
        return false;
    }

    public boolean isInGame(Player player) {
        return getByPlayer(player) != null;
    }

    public void delete(BattleRoyaleGame game) {
        this.games.remove(game);
    }

    public BattleRoyaleGame getById(String id) {
        for(BattleRoyaleGame game : this.games) {
            if(game.getId().equalsIgnoreCase(id)) {
                return game;
            }
        }
        return null;
    }

    public boolean existsById(String id) {
        return getById(id) != null;
    }

}
