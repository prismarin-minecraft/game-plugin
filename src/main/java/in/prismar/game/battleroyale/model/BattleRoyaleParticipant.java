package in.prismar.game.battleroyale.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
public class BattleRoyaleParticipant {

    private Player player;

    @Setter
    private BattleRoyaleParticipantState state;

    public BattleRoyaleParticipant(Player player) {
        this.player = player;
        this.state = BattleRoyaleParticipantState.ALIVE;
    }

    public enum BattleRoyaleParticipantState {

        ALIVE, KNOCKED, DEAD

    }
}
