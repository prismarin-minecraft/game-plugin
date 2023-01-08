package in.prismar.game.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.prismar.api.PrismarinApi;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.game.web.route.GetWebRoute;
import in.prismar.game.web.route.WebRoute;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class WebServer {

    private String basePath;

    private Javalin javalin;
    private List<WebRoute<?>> routes;

    private Gson gson;

    public WebServer(String basePath, int port) {
        this.basePath = basePath;
        this.gson = new GsonBuilder().create();
        this.routes = new ArrayList<>();
        this.javalin = Javalin.create().start(port);
    }

    public WebServer addRoute(WebRoute<?> route) {
        this.routes.add(route);
        return this;
    }

    public void initializePaths() {
        for(WebRoute<?> route : this.routes) {
            if(route instanceof GetWebRoute<?>) {
                javalin.get("/"+ basePath + route.getPath(), context -> {
                    context.header("Content-Type", "application/json");
                    context.result(gson.toJson(route.onRoute(context)));
                });
            }
        }
    }
}
