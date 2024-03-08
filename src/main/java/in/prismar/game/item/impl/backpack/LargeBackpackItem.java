package in.prismar.game.item.impl.backpack;

import org.bukkit.Material;

public class LargeBackpackItem extends BackpackItem{
    public LargeBackpackItem() {
        super("LargeBackpack", Material.FEATHER, "§6Large Backpack", 5);
        setCustomModelData(13);
    }
}
