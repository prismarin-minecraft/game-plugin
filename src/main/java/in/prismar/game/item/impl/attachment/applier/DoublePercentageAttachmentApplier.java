package in.prismar.game.item.impl.attachment.applier;

import in.prismar.game.item.impl.attachment.AttachmentApplier;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class DoublePercentageAttachmentApplier implements AttachmentApplier<Double> {

    private int percentage;
    private ApplierOperation operation;

    public DoublePercentageAttachmentApplier(int percentage, ApplierOperation operation) {
        this.percentage = percentage;
        this.operation = operation;
    }

    @Override
    public Double apply(Double current) {
        double percent = (current / 100.0) * (double) percentage;
        return operation == ApplierOperation.ADD ? current + percent : current - percent;
    }
}
