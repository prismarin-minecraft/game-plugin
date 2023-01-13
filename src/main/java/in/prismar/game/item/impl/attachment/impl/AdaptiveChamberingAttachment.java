package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.applier.ApplierOperation;
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
public class AdaptiveChamberingAttachment extends Attachment {

    public AdaptiveChamberingAttachment() {
        super("AdaptiveChambering", Material.LEVER, "§7Adaptive Chambering");
        setCustomModelData(2);
        addAllowedTypes(GunType.AR, GunType.SMG, GunType.SHOTGUN);
        registerApplier(AttachmentModifier.FIRE_RATE, new IntPercentageAttachmentApplier(25));
        registerApplier(AttachmentModifier.SPREAD, new DoublePercentageAttachmentApplier(30, ApplierOperation.ADD));
        registerApplier(AttachmentModifier.SNEAK_SPREAD, new DoublePercentageAttachmentApplier(10, ApplierOperation.ADD));

        addLore("§c");
        addLore(" §8╔ §7Fire rate§8: §a+25%");
        addLore(" §8╠ §7Spread§8: §c+30%");
        addLore(" §8╚ §7Sneak spread§8: §c+10%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
