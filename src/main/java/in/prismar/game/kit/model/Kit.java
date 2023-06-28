package in.prismar.game.kit.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.item.container.ItemContainer;
import in.prismar.library.spigot.item.container.ItemsContainer;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter @Setter
public class Kit extends StringRepositoryEntity {

    private ItemContainer icon;
    private ItemsContainer items;
    private int cooldownInSeconds;
    private int weight;

    @Override
    public String toString() {
        return getId();
    }
}
