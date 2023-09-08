package in.prismar.game.item.impl.tea;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeaType {

    ORE("§cOre Tea", 1);

    private final String displayName;
    private final int customModelData;
}
