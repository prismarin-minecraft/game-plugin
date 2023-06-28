package in.prismar.game.kit.repository;

import com.google.gson.reflect.TypeToken;
import in.prismar.game.kit.model.Kit;
import in.prismar.library.file.gson.GsonCompactRepository;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FileKitRepository extends GsonCompactRepository<Kit> implements KitRepository {
    public FileKitRepository(String directory) {
        super(directory.concat("kits.json"), new TypeToken<Map<String, Kit>>(){});
        for(Kit kit : getEntity().values()) {
            kit.getIcon().deserialize();
            if(kit.getItems() != null) {
                kit.getItems().deserialize();
            }
        }
    }
}
