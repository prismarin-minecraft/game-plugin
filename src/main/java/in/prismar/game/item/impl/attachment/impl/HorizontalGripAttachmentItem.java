package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.applier.ApplierOperation;
import in.prismar.game.item.impl.attachment.applier.DoublePercentageAttachmentApplier;
import in.prismar.game.item.impl.attachment.template.GripAttachment;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class HorizontalGripAttachmentItem extends GripAttachment {

    public HorizontalGripAttachmentItem() {
        super("HorizontalGrip", Material.LEVER, "§cHorizontal Grip");
        setCustomModelData(4);
        addAllowedTypes(GunType.values());
        registerApplier(AttachmentModifier.SPREAD, new DoublePercentageAttachmentApplier(10, ApplierOperation.SUBTRACT));
        registerApplier(AttachmentModifier.SNEAK_SPREAD, new DoublePercentageAttachmentApplier(10, ApplierOperation.SUBTRACT));

        addLore("§c");
        addLore(" §8╔ §7Spread§8: §a-10%");
        addLore(" §8╠ §7Sneak spread§8: §a-10%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
