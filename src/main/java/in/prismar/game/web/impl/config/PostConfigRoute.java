package in.prismar.game.web.impl.config;


import com.google.common.reflect.TypeToken;
import in.prismar.game.Game;
import in.prismar.api.configuration.node.event.ConfigRefreshEvent;
import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.route.PostWebRoute;
import io.javalin.http.Context;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PostConfigRoute extends PostWebRoute<Boolean> {

    private final Game game;

    public PostConfigRoute(Game game) {
        super("config");
        this.game = game;
    }

    @Override
    public Boolean onRoute(Context context) {
        Map<String, ConfigNode> nodeMap = game.getWebServer().getGson().fromJson(context.body(), new TypeToken<Map<String, ConfigNode>>() {}.getType());
        game.getConfigNodeFile().refreshNodeIds(nodeMap);
        for (ConfigNode node : nodeMap.values()) {
            game.getConfigNodeFile().updateNode(node);
        }
        game.getConfigNodeFile().getEventBus().publish(new ConfigRefreshEvent());
        return true;
    }



    @Override
    public String[] isSecured() {
        return new String[]{"*"};
    }
}
