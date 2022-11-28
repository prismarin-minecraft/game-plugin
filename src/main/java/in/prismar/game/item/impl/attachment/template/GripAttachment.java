package in.prismar.game.item.impl.attachment.template;

import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.gun.Gun;
import org.bukkit.Material;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GripAttachment extends Attachment {
    public GripAttachment(String id, Material material, String displayName) {
        super(id, material, displayName);
    }

    @Override
    public boolean isAllowedToAttach(Gun gun, List<Attachment> currentAttachments) {
        boolean allowed = super.isAllowedToAttach(gun, currentAttachments);
        if(!allowed) {
            return false;
        }
        for(Attachment attachment : currentAttachments) {
            if(attachment instanceof GripAttachment) {
                return false;
            }
        }
        return true;
    }
}
