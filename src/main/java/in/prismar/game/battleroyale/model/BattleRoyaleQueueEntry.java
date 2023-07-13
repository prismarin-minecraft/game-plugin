package in.prismar.game.battleroyale.model;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class BattleRoyaleQueueEntry {

    private Map<UUID, Player> players;
    private Set<UUID> invites;

    public BattleRoyaleQueueEntry() {
        this.players = new LinkedHashMap<>();
        this.invites = new HashSet<>();
    }

}
