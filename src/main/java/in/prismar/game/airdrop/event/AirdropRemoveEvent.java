package in.prismar.game.airdrop.event;

import in.prismar.game.airdrop.AirDrop;
import in.prismar.library.common.event.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AllArgsConstructor
@Getter
public class AirdropRemoveEvent implements Event {

    private AirDrop airDrop;
}
