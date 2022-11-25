package in.prismar.game.item.reader;

import lombok.Data;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class BaseReaderData {

    private String id;
    private Material material;
    private String displayName;

}
