package in.prismar.game.web.impl.banner.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.DyeColor;
import org.bukkit.block.banner.PatternType;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@NoArgsConstructor
@Getter @Setter
public class BannerPattern {

    private DyeColor color;
    private PatternType pattern;
}
