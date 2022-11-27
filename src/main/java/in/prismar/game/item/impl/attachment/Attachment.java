package in.prismar.game.item.impl.attachment;

import in.prismar.game.item.CustomItem;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Attachment extends CustomItem {

    private final Map<AttachmentModifier, List<AttachmentApplier>> appliers;

    public Attachment(String id, Material material, String displayName) {
        super(id, material, displayName);
        this.appliers = new HashMap<>();
    }


    public void registerApplier(AttachmentModifier modifier, AttachmentApplier applier) {
        if(!appliers.containsKey(modifier)) {
            appliers.put(modifier, new ArrayList<>());
        }
        appliers.get(modifier).add(applier);
    }

    public <T> T apply(AttachmentModifier modifier, T current) {
        T value = current;
        if(appliers.containsKey(modifier)) {
            for(AttachmentApplier<T> applier : appliers.get(modifier)) {
                value = applier.apply(value);
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return getId();
    }
}
