package in.prismar.game.interactable.repository;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.model.Interactable;
import in.prismar.library.file.gson.GsonCompactRepository;
import in.prismar.library.file.gson.GsonDeserializerWithInheritance;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.file.GsonLocationAdapter;
import org.bukkit.Location;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class FileInteractableRepository extends GsonCompactRepository<Interactable> implements InteractableRepository {
    public FileInteractableRepository(InteractableService service) {
        super(service.getGame().getDefaultDirectory().concat("interactables.json"), new TypeToken<Map<String, Interactable>>(){});
        for(Interactable interactable : getEntity().values()) {
            interactable.onLoad(service);
        }
    }

    @Override
    public void intercept(GsonBuilder builder) {
        builder.registerTypeAdapter(Interactable.class, new GsonDeserializerWithInheritance<Interactable>());
        builder.registerTypeAdapter(Location.class, new GsonLocationAdapter());
    }
}
