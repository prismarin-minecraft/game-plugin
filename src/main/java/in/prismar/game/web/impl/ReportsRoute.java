package in.prismar.game.web.impl;

import in.prismar.api.PrismarinApi;
import in.prismar.api.report.ReportProvider;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.web.impl.response.PlayerResponse;
import in.prismar.game.web.route.DeleteWebRoute;
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
public class ReportsRoute extends DeleteWebRoute<String> {


    public ReportsRoute() {
        super("reports/{id}");
    }

    @Override
    public String onRoute(Context context) {
        final String id = context.pathParam("id");
        ReportProvider provider = PrismarinApi.getProvider(ReportProvider.class);
        System.out.println("Deleting cached report: " + id);
        provider.deleteById(id);
        return id;
    }
}
