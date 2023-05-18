package in.prismar.game.web.impl.banner;

import in.prismar.api.PrismarinApi;
import in.prismar.api.clan.Clan;
import in.prismar.api.clan.ClanProvider;
import in.prismar.game.Game;
import in.prismar.game.web.WebServer;
import in.prismar.game.web.impl.banner.request.BannerRequest;
import in.prismar.game.web.impl.banner.response.BannerResponse;
import in.prismar.game.web.route.PostWebRoute;
import io.javalin.http.Context;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class BannerRoute extends PostWebRoute<BannerResponse> {

    private final Game game;
    private final WebServer server;

    public BannerRoute(Game game, WebServer server) {
        super("banner");
        this.game = game;
        this.server = server;
    }

    @Override
    public BannerResponse onRoute(Context context) {
        BannerRequest banner = server.getGson().fromJson(context.body(), BannerRequest.class);
        ClanProvider<Clan> clanProvider = PrismarinApi.getProvider(ClanProvider.class);
        return new BannerResponse(clanProvider.refreshBanner(banner.getId()));
    }
}
