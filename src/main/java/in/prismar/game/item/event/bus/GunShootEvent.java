package in.prismar.game.item.event.bus;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.library.common.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class GunShootEvent implements Event {

    private GunPlayer gunPlayer;
    private Gun gun;

    @Setter
    private boolean cancelled;
}
