package in.prismar.game.battleroyale.arena.droptable;

import com.google.gson.reflect.TypeToken;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.spigot.item.container.ItemContainer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DropTableFile extends GsonFileWrapper<List<DropTableItem>> {

    private List<ItemStack> items;

    public DropTableFile(String directory) {
        super(directory.concat("droptable.json"), new TypeToken<List<DropTableItem>>(){}.getType());
        load();
        if(getEntity() == null) {
            setEntity(new ArrayList<>());
        } else {
            for(DropTableItem item : getEntity()) {
                item.getItem().deserialize();
            }
        }
        generateRandomChanceItems();
    }

    public ItemStack find(ItemStack stack) {
        for(DropTableItem item : getEntity()) {
            if(item.getItem().getItem().isSimilar(stack)) {
                return item.getItem().getItem();
            }
        }
        return null;
    }

    public DropTableItem register(ItemStack stack, double chance) {
        DropTableItem item = new DropTableItem(new ItemContainer(stack), chance);
        this.getEntity().add(item);
        save();
        generateRandomChanceItems();
        return item;
    }

    public void generateRandomChanceItems() {
        this.items = new ArrayList<>();
        for(DropTableItem item : getEntity()) {
            for (int i = 0; i < (int)item.getChance(); i++) {
                this.items.add(item.getItem().getItem());
            }
        }
    }

    public ItemStack findRandomItem() {
        return this.items.get(MathUtil.random(this.items.size() - 1));
    }
}
