package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.applier.ApplierOperation;
import in.prismar.game.item.impl.attachment.applier.DoublePercentageAttachmentApplier;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BarrelAttachmentItem extends Attachment {

    public BarrelAttachmentItem() {
        super("Barrel", Material.REDSTONE_TORCH, "§aBarrel");
        addAllowedTypes(GunType.AR, GunType.SMG, GunType.PISTOL);
        registerApplier(AttachmentModifier.SPREAD, new DoublePercentageAttachmentApplier(25, ApplierOperation.ADD));
        registerApplier(AttachmentModifier.RANGE, new DoublePercentageAttachmentApplier(25, ApplierOperation.ADD));

        addLore("§c");
        addLore(" §7Range§8: §a+25%");
        addLore(" §7Spread§8: §c+25%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
