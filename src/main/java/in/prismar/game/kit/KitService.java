package in.prismar.game.kit;

import in.prismar.game.Game;
import in.prismar.game.kit.model.Kit;
import in.prismar.game.kit.repository.FileKitRepository;
import in.prismar.game.kit.repository.KitRepository;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class KitService {

    private KitRepository repository;

    public KitService(Game game) {
        this.repository = new FileKitRepository(game.getDefaultDirectory());
    }

    public Kit create(String id, ItemStack stack, int cooldownInSeconds) {
        Kit kit = new Kit();
        kit.setId(id);
        kit.setIcon(new ItemContainer(stack));
        kit.setCooldownInSeconds(cooldownInSeconds);
        return repository.create(kit);
    }

    public void giveRespawnKit(Player player) {
        Optional<Kit> optional = findRespawnKit();
        if(optional.isEmpty()) return;
        Kit kit = optional.get();
        if(kit.getItems() == null) return;
        for(ItemStack stack : kit.getItems().getItem()) {
            giveItem(player, stack);
        }
    }

    public void giveStarterKit(Player player) {
        Optional<Kit> optional = findStarterKit();
        if(optional.isEmpty()) return;
        Kit kit = optional.get();
        if(kit.getItems() == null) return;
        for(ItemStack stack : kit.getItems().getItem()) {
            giveItem(player, stack);
        }
    }

    private void giveItem(Player player, ItemStack stack) {
        if(stack == null) return;
        if(stack.getType() == Material.AIR) return;
        if(stack.getType().name().contains("HELMET")) {
            player.getInventory().setHelmet(stack.clone());
        } else if(stack.getType().name().contains("CHESTPLATE")) {
            player.getInventory().setChestplate(stack.clone());
        } else if(stack.getType().name().contains("LEGGINGS")) {
            player.getInventory().setLeggings(stack.clone());
        } else if(stack.getType().name().contains("BOOTS")) {
            player.getInventory().setBoots(stack.clone());
        } else {
            player.getInventory().addItem(stack.clone());
        }
    }

    private Optional<Kit> findRespawnKit() {
        return repository.findAll().stream().filter(kit -> kit.isRespawn()).findFirst();
    }

    private Optional<Kit> findStarterKit() {
        return repository.findAll().stream().filter(kit -> kit.isStarter()).findFirst();
    }
}
