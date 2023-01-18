package in.prismar.game.item.reader.impl;

import in.prismar.game.item.model.Skin;
import in.prismar.game.item.reader.BaseReaderData;
import lombok.Data;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class MeleeData extends BaseReaderData {

    private int damage;
    private MeleeAttackSpeed attackSpeed;

    private String previewImage;

    private List<Skin> skins;
}
