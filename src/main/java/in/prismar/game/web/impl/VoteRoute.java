package in.prismar.game.web.impl;

import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.placeholder.PlaceholderStore;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.web.impl.response.PlayerResponse;
import in.prismar.game.web.impl.response.VoteResponse;
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
public class VoteRoute extends GetWebRoute<VoteResponse> {

    private final PlaceholderStore store;
    private final ConfigStore configStore;

    public VoteRoute() {
        super("vote/");
        this.store = PrismarinApi.getProvider(PlaceholderStore.class);
        this.configStore = PrismarinApi.getProvider(ConfigStore.class);
    }

    @Override
    public VoteResponse onRoute(Context context) {
        int count = store.existsPlaceholder("vote.party") ? store.getPlaceholder("vote.party") : 0;
        int maxCount = Integer.valueOf(configStore.getProperty("vote.party.max"));
        return new VoteResponse(count, maxCount);
    }
}
