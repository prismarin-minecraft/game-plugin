package in.prismar.game.airdrop.loot;

import com.google.common.reflect.TypeToken;
import in.prismar.library.file.gson.GsonFileWrapper;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class AirDropLootTable extends GsonFileWrapper<List<AirDropItem>> {

    public AirDropLootTable(String dir) {
        super(dir.concat("airdrop.json"), new TypeToken<List<AirDropItem>>(){}.getType());
        load();
        if(getEntity() == null){
            setEntity(new ArrayList<>());
        } else {
            for (AirDropItem item : getEntity()) {
                item.getItem().deserialize();
            }
        }
    }



    public Inventory generateRandomInventory(String title, int rows, int amount){
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);
        List<ItemStack> items = generateRandomItems(amount);
        for (ItemStack item : items) {
            inventory.addItem(item);
        }
        return inventory;
    }


    public List<ItemStack> generateRandomItems(int amount) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            Optional<AirDropItem> optional = findRandomItem();
            if(optional.isPresent()){
                items.add(optional.get().getItem().getItem().clone());
            }
        }
        return items;
    }

    public Optional<AirDropItem> findRandomItem() {
        double sum = getEntity().stream().mapToDouble(AirDropItem::getChance).sum();
        double random = Math.random() * sum;
        for(AirDropItem loot : this.getEntity()) {
            random -= loot.getChance();
            if(random < 0) {
                return Optional.of(loot);
            }
        }
        return Optional.empty();
    }
}
