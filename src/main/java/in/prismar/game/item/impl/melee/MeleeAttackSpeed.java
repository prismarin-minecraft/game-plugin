package in.prismar.game.item.impl.melee;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MeleeAttackSpeed {

    FAST("Fast", 0, 250),
    NORMAL("Normal", 1, 500),
    SLOW("Slow", 3, 1000);

    private final String fancyName;
    private final int amplifier;
    private final long countdown;

}