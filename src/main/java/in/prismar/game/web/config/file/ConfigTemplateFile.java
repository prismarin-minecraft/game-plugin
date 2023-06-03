package in.prismar.game.web.config.file;

import com.google.gson.reflect.TypeToken;
import in.prismar.game.Game;
import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.config.model.ConfigNodeType;
import in.prismar.library.file.gson.GsonFileWrapper;
import in.prismar.library.meta.anno.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
public class ConfigTemplateFile extends GsonFileWrapper<Map<String, ConfigNode>> {
    public ConfigTemplateFile(Game game) {
        super(game.getDefaultDirectory().concat("config_templates.json"), new TypeToken<Map<String, ConfigNode>>(){}.getType());
        load();
        if(getEntity() == null) {
            setEntity(new HashMap<>());
            save();
        }
    }

    public ConfigNode getTemplate(String id) {
        return getEntity().get(id.toLowerCase());
    }

    public boolean existsTemplate(String id) {
        return getEntity().containsKey(id.toLowerCase());
    }

    public void registerTemplate(String id, ConfigNode node) {
        this.getEntity().put(id.toLowerCase(), node);
        save();
    }
}
