package in.prismar.game.battleroyale.model;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BattleRoyaleTeam {

    private Map<UUID, BattleRoyaleParticipant> participants;

    public BattleRoyaleTeam() {
        this.participants = new HashMap<>();
    }

    public void registerParticipant(Player player) {
        this.participants.put(player.getUniqueId(), new BattleRoyaleParticipant(player));
    }
}
