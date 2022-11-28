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
public class ExtendedMagazineAttachmentItem extends Attachment {

    public ExtendedMagazineAttachmentItem() {
        super("ExtendedMagazine", Material.NAME_TAG, "§eExtended Magazine");
        addAllowedTypes(GunType.values());
        registerApplier(AttachmentModifier.MAX_AMMO, new IntPercentageAttachmentApplier(25));

        addLore("§c");
        addLore(" §7Max ammo§8: §a+25%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
