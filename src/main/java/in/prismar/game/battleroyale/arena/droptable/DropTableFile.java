package in.prismar.game.battleroyale.arena.droptable;

import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.spigot.item.container.ItemContainer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DropTableFile extends GsonFileWrapper<List<DropTableItem>> {
    public DropTableFile(String directory) {
        super(directory.concat("droptable.json"), ArrayList.class);
        load();
        if(getEntity() == null) {
            setEntity(new ArrayList<>());
        } else {
            for(DropTableItem item : getEntity()) {
                item.getItem().deserialize();
            }
        }
    }

    public DropTableItem register(ItemStack stack, double chance) {
        DropTableItem item = new DropTableItem(new ItemContainer(stack), chance);
        this.getEntity().add(item);
        save();
        return item;
    }
}
