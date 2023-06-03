package in.prismar.game.web.impl.config;


import in.prismar.game.Game;
import in.prismar.game.web.config.model.ConfigNode;
import in.prismar.game.web.route.GetWebRoute;
import io.javalin.http.Context;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class GetConfigRoute extends GetWebRoute<Map<String, ConfigNode>> {

    private final Game game;

    public GetConfigRoute(Game game) {
        super("config");
        this.game = game;
    }

    @Override
    public Map<String, ConfigNode> onRoute(Context context) {
        return game.getConfigNodeFile().getEntity();
    }
}
