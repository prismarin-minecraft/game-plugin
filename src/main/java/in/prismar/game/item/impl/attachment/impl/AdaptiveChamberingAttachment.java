package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
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
        super("AdaptiveChambering", Material.LIGHT_GRAY_DYE, "§7Adaptive Chambering");
        addAllowedTypes(GunType.AR, GunType.SMG, GunType.SHOTGUN);
        registerApplier(AttachmentModifier.FIRE_RATE, new IntPercentageAttachmentApplier(50));

        addLore("§c");
        addLore(" §7Fire rate§8: §a+50%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
