package in.prismar.game.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.prismar.api.PrismarinApi;
import in.prismar.api.web.WebAuthenticator;
import in.prismar.game.web.route.DeleteWebRoute;
import in.prismar.game.web.route.GetWebRoute;
import in.prismar.game.web.route.PostWebRoute;
import in.prismar.game.web.route.WebRoute;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.bundled.CorsPluginConfig;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class WebServer {

    private final String basePath;

    private Javalin javalin;
    private final List<WebRoute<?>> routes;

    private final Gson gson;

    private final boolean devMode;

    public WebServer(String basePath, int port, boolean isDevMode) {
        this.basePath = basePath;
        this.gson = new GsonBuilder().create();
        this.routes = new ArrayList<>();
        this.devMode = isDevMode;
        if(!isDevMode) {
            this.javalin = Javalin.create(config -> {
                config.plugins.enableCors(cors -> {
                    cors.add(CorsPluginConfig::anyHost);
                });
            }).start(port);
        }

    }

    public WebServer addRoute(WebRoute<?> route) {
        this.routes.add(route);
        return this;
    }

    public void initializePaths() {
        if(this.devMode) {
            return;
        }
        WebAuthenticator authenticator = PrismarinApi.getProvider(WebAuthenticator.class);
        for(WebRoute<?> route : this.routes) {
            if(route instanceof GetWebRoute<?>) {
                javalin.get("/"+ basePath + route.getPath(), context -> {
                    if(!isAuth(authenticator, route, context)) {
                        return;
                    }
                    context.header("Content-Type", "application/json");
                    context.result(gson.toJson(route.onRoute(context)));
                });
            } else if(route instanceof PostWebRoute<?>) {
                javalin.post("/" + basePath + route.getPath(), context -> {
                    if(!isAuth(authenticator, route, context)) {
                        return;
                    }
                    context.header("Content-Type", "application/json");
                    context.result(gson.toJson(route.onRoute(context)));
                });
            } else if(route instanceof DeleteWebRoute<?>) {
                javalin.delete("/" + basePath + route.getPath(), context -> {
                    if(!isAuth(authenticator, route, context)) {
                        return;
                    }
                    context.header("Content-Type", "application/json");
                    context.result(gson.toJson(route.onRoute(context)));
                });
            }
        }
    }

    private boolean isAuth(WebAuthenticator authenticator, WebRoute<?> route, Context context) {
        String[] secured = route.isSecured();
        if(secured.length != 0) {
            final String header = context.header("Authorization");
            if(header == null) {
                System.out.println("Header is null");
                context.status(401);
                context.result();
                return false;
            }
            final String token = header.trim().replace("Bearer", "").replace(" ", "");
            if(!authenticator.isAuth(token, secured)) {
                System.out.println("Token auth failed: " + token);
                context.status(401);
                context.result();
                return false;
            }
        }
        return true;
    }
}
