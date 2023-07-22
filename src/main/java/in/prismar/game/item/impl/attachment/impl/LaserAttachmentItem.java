package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.applier.ApplierOperation;
import in.prismar.game.item.impl.attachment.applier.DoublePercentageAttachmentApplier;
import in.prismar.game.item.impl.attachment.template.GripAttachment;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

import java.util.Arrays;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class LaserAttachmentItem extends GripAttachment {

    public LaserAttachmentItem() {
        super("HorizontalGrip", Material.LEVER, "§cLaser");
        setCustomModelData(8);
        addAllowedTypes(Arrays.stream(GunType.values()).filter(type -> type != GunType.SPECIAL).toList().toArray(new GunType[0]));
        registerApplier(AttachmentModifier.SPREAD, new DoublePercentageAttachmentApplier(25, ApplierOperation.SUBTRACT));
        registerApplier(AttachmentModifier.SNEAK_SPREAD, new DoublePercentageAttachmentApplier(25, ApplierOperation.SUBTRACT));

        addLore("§c");
        addLore(" §8╔ §7Spread§8: §a-20%");
        addLore(" §8╠ §7Sneak spread§8: §a-20%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
