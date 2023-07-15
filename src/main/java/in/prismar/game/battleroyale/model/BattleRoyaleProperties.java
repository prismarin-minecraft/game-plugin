package in.prismar.game.battleroyale.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BattleRoyaleProperties {

    private int playersSize = 20;
    private int teamSize = 1;

    private int queueTime = 299;
    private int warmUpTime = 100;
    private int inGameTime = 1800;
}
