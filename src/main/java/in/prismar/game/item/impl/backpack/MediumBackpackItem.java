package in.prismar.game.item.impl.backpack;

import org.bukkit.Material;

public class MediumBackpackItem extends BackpackItem{
    public MediumBackpackItem() {
        super("MediumBackpack", Material.FEATHER, "§6Medium Backpack", 3);
        setCustomModelData(12);
    }
}
