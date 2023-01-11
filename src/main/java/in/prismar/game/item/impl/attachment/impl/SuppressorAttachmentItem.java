package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.applier.ApplierOperation;
import in.prismar.game.item.impl.attachment.applier.BooleanAttachmentApplier;
import in.prismar.game.item.impl.attachment.applier.DoublePercentageAttachmentApplier;
import in.prismar.game.item.impl.attachment.applier.IntPercentageAttachmentApplier;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class SuppressorAttachmentItem extends Attachment {

    public SuppressorAttachmentItem() {
        super("Suppressor", Material.LEVER, "§dSuppressor");
        setCustomModelData(1);
        addAllowedTypes(GunType.AR, GunType.SMG, GunType.PISTOL, GunType.SNIPER);
        registerApplier(AttachmentModifier.SOUND, new BooleanAttachmentApplier(false));
        registerApplier(AttachmentModifier.RANGE, new DoublePercentageAttachmentApplier(15, ApplierOperation.SUBTRACT));

        addLore("§c");
        addLore(" §7Sound suppression");
        addLore(" §7Range§8: §c-25%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
