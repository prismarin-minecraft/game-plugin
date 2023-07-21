package in.prismar.game.item.impl.attachment;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.type.GunType;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class Attachment extends CustomItem {

    private final Map<AttachmentModifier, List<AttachmentApplier>> appliers;
    private final Set<GunType> allowedTypes;

    public Attachment(String id, Material material, String displayName) {
        super(id, material, displayName);
        this.allowedTypes = new HashSet<>();
        this.appliers = new HashMap<>();
    }

    public Attachment addAllowedTypes(GunType... types) {
        this.allowedTypes.addAll(Arrays.asList(types));
        return this;
    }

    protected void addAllowedTypesLore() {
        List<String> types = allowedTypes.stream().map(type -> type.getDisplayName()).sorted().collect(Collectors.toList());
        addLore(" §8╚ §7Gun types§8: §3" + Joiner.on("§8, §3").join(
              types
        ));
    }

    public boolean isAllowedToAttach(Gun gun, List<Attachment> currentAttachments) {
        for(Attachment current : currentAttachments) {
            if(current.getId().equals(getId())) {
                return false;
            }
        }
        return this.allowedTypes.contains(gun.getType());
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
