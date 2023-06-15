package in.prismar.game.interactable.model;

import com.google.gson.annotations.SerializedName;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.InteractableType;
import in.prismar.library.common.repository.entity.StringRepositoryEntity;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Data
public class Interactable extends StringRepositoryEntity {

    @SerializedName("class")
    private String className;


    private Location location;

    public Interactable() {
        this.className = getClass().getName();
    }

    public void onLoad(InteractableService service) {}
    public void onInteract(InteractableService service, Player player) {}

    @Override
    public String toString() {
        return getId();
    }
}
