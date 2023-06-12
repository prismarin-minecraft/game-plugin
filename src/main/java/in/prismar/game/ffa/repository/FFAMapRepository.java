package in.prismar.game.ffa.repository;

import in.prismar.game.ffa.model.FFAMap;
import in.prismar.library.common.repository.Repository;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface FFAMapRepository extends Repository<String, FFAMap> {

    FFAMap create(String id, ItemStack icon);
}
