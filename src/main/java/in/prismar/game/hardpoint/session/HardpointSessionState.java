package in.prismar.game.hardpoint.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@RequiredArgsConstructor
public enum HardpointSessionState {

    IDLE("§fIdle", Material.WHITE_WOOL),
    CAPTURED("§aCaptured", Material.GREEN_WOOL),
    CONTESTED("§eContested", Material.YELLOW_WOOL);

    private final String title;
    private final Material material;
}
