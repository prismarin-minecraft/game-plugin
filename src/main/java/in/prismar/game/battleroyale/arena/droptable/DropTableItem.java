package in.prismar.game.battleroyale.arena.droptable;

import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DropTableItem {

    private ItemContainer item;
    private double chance;
}
