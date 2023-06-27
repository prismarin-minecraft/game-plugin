package in.prismar.game.kit;

import in.prismar.game.Game;
import in.prismar.game.kit.model.Kit;
import in.prismar.game.kit.repository.FileKitRepository;
import in.prismar.game.kit.repository.KitRepository;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.container.ItemContainer;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

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
}
