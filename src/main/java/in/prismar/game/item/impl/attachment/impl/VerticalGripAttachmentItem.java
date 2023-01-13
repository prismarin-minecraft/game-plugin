package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.template.GripAttachment;
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
public class VerticalGripAttachmentItem extends GripAttachment {

    public VerticalGripAttachmentItem() {
        super("VerticalGrip", Material.LEVER, "§cVertical Grip");
        setCustomModelData(7);
        addAllowedTypes(GunType.AR, GunType.SMG);
        registerApplier(AttachmentModifier.SPREAD, new DoublePercentageAttachmentApplier(25, ApplierOperation.SUBTRACT));

        addLore("§c");
        addLore(" §8╔ §7Spread§8: §a-25%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
