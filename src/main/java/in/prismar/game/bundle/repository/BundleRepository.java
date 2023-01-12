package in.prismar.game.bundle.repository;

import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.common.repository.Repository;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface BundleRepository extends Repository<String, Bundle> {

    Bundle create(String id, String display, boolean seasonal, ItemStack icon);
}
