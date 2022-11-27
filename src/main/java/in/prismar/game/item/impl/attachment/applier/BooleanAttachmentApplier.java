package in.prismar.game.item.impl.attachment.applier;

import in.prismar.game.item.impl.attachment.AttachmentApplier;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BooleanAttachmentApplier implements AttachmentApplier<Boolean> {

    private boolean state;

    public BooleanAttachmentApplier(boolean state) {
        this.state = state;
    }

    @Override
    public Boolean apply(Boolean current) {
        return state;
    }
}
