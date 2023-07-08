package in.prismar.game.battleroyale.model;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class BattleRoyaleTeam {

    private Map<UUID, BattleRoyaleParticipant> participants;

    public BattleRoyaleTeam() {
        this.participants = new HashMap<>();
    }
}
