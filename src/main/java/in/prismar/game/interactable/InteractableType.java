package in.prismar.game.interactable;

import in.prismar.game.interactable.model.Interactable;
import in.prismar.game.interactable.model.button.Button;
import in.prismar.game.interactable.model.keycode.Keycode;
import in.prismar.game.interactable.model.keylock.KeyLock;
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
public enum InteractableType {

    KEYCODE(Keycode.class),
    BUTTON(Button.class),
    KEYLOCK(KeyLock.class);

    private final Class<? extends Interactable> type;
}
