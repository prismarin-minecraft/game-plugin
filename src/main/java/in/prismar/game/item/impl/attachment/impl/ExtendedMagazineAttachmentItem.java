package in.prismar.game.item.impl.attachment.impl;

import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.attachment.applier.IntPercentageAttachmentApplier;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;

import java.util.Arrays;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ExtendedMagazineAttachmentItem extends Attachment {

    public ExtendedMagazineAttachmentItem() {
        super("ExtendedMagazine", Material.LEVER, "§eExtended Magazine");
        setCustomModelData(5);
        addAllowedTypes(Arrays.stream(GunType.values()).filter(type -> type != GunType.SHOTGUN).toList().toArray(new GunType[0]));
        registerApplier(AttachmentModifier.MAX_AMMO, new IntPercentageAttachmentApplier(25));

        addLore("§c");
        addLore(" §8╔ §7Max ammo§8: §a+25%");
        addAllowedTypesLore();
        addLore("§c");
    }
}
