package in.prismar.game.bundle.model;

import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import in.prismar.library.spigot.item.container.ItemContainer;
import in.prismar.library.spigot.item.container.ItemsContainer;
import lombok.Data;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class Bundle extends StringRepositoryEntity {

    private String displayName;
    private boolean seasonal;

    private double balance;
    private ItemContainer icon;

    private ItemsContainer container;

    @Override
    public String toString() {
        return getId();
    }
}
