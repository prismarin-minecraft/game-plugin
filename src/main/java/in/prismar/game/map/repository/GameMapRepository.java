package in.prismar.game.map.repository;

import in.prismar.game.map.model.GameMap;
import in.prismar.library.common.repository.Repository;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface GameMapRepository extends Repository<String, GameMap> {

    GameMap create(String id, ItemStack icon);
}
