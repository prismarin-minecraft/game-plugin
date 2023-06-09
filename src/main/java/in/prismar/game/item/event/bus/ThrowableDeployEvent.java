package in.prismar.game.item.event.bus;

import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.player.GunPlayer;
import in.prismar.game.item.impl.throwable.ThrowableItem;
import in.prismar.library.common.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@AllArgsConstructor
public class ThrowableDeployEvent implements Event {

    private Player player;
    private ThrowableItem item;


    @Setter
    private boolean cancelled;
}
