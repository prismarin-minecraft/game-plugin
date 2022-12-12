package in.prismar.game.bundle.repository;

import com.google.gson.reflect.TypeToken;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.file.gson.GsonCompactRepository;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class FileBundleRepository extends GsonCompactRepository<Bundle> implements BundleRepository {

    public FileBundleRepository(String directory) {
        super(directory.concat("bundles.json"), new TypeToken<Map<String, Bundle>>(){});
        for(Bundle bundle : getEntity().values()) {
            if(bundle.getContainer() != null) {
                bundle.getContainer().deserialize();
            }
        }
    }

    @Override
    public Bundle create(String id, String display) {
        Bundle bundle = new Bundle();
        bundle.setId(id);
        bundle.setDisplayName(display);
        return create(bundle);
    }
}
