package in.prismar.game.item.reader;

import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface CustomItemReaderSource {

    List<CustomItem> read(CustomItemRegistry registry);

}
