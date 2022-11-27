package in.prismar.game.item.impl.attachment.applier;

import in.prismar.game.item.impl.attachment.AttachmentApplier;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class IntPercentageAttachmentApplier implements AttachmentApplier<Integer> {

    private int percentage;

    public IntPercentageAttachmentApplier(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public Integer apply(Integer current) {
        double percent = ((double)current / 100.0) * (double) percentage;
        return current + (int)Math.round(percent);
    }
}
