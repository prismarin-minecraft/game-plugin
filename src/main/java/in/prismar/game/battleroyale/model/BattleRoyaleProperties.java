package in.prismar.game.battleroyale.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleRoyaleProperties {

    private int playersSize;
    private int teamSize;

    private int queueTime;
    private int warmUpTime;
    private int inGameTime;
}
