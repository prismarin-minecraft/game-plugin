package in.prismar.game.item.impl.backpack;

import org.bukkit.Material;

public class SmallBackpackItem extends BackpackItem{
    public SmallBackpackItem() {
        super("SmallBackpack", Material.FEATHER, "§6Small Backpack", 1);
        setCustomModelData(11);
    }
}
