package in.prismar.game.web.impl;

import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.web.impl.response.PlayerResponse;
import in.prismar.game.web.route.GetWebRoute;
import io.javalin.http.Context;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PlayerRoute extends GetWebRoute<PlayerResponse> {

    private final FFAFacade mapFacade;


    public PlayerRoute(FFAFacade mapFacade) {
        super("player/{id}");
        this.mapFacade = mapFacade;
    }

    @Override
    public PlayerResponse onRoute(Context context) {
        final String id = context.pathParam("id");
        Player player = Bukkit.getPlayer(UUID.fromString(id));
        if(player == null) {
            return new PlayerResponse(false, null,0);
        }
        final String game = mapFacade.isInMap(player.getUniqueId()) ? "FFA" : "Lobby";
        return new PlayerResponse(true, game,0);
    }
}
