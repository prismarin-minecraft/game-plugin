package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.applier.ApplierOperation;
import in.prismar.game.item.impl.attachment.applier.DoublePercentageAttachmentApplier;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BipodAttachmentItem extends Attachment {

    public BipodAttachmentItem() {
        super("Bipod", Material.TRIPWIRE_HOOK, "§cBipod");
        registerApplier(AttachmentModifier.SNEAK_SPREAD, new DoublePercentageAttachmentApplier(25, ApplierOperation.SUBTRACT));

        addLore("§c");
        addLore(" §7Sneak spread§8: §a-25%");
        addLore("§c");
    }
}
