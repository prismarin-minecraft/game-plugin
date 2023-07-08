package in.prismar.game.battleroyale.model;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BattleRoyaleQueueEntry {

    private Map<UUID, Player> players;

    public BattleRoyaleQueueEntry() {
        this.players = new LinkedHashMap<>();
    }
}
