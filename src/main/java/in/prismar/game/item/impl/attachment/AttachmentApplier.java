package in.prismar.game.item.impl.attachment;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public interface AttachmentApplier<T> {

    T apply(T current);
}
