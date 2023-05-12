package in.prismar.game.web.impl.banner;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.web.WebServer;
import in.prismar.game.web.impl.banner.request.Banner;
import in.prismar.game.web.impl.banner.request.BannerPattern;
import in.prismar.game.web.impl.banner.response.BannerResponse;
import in.prismar.game.web.impl.response.PlayerResponse;
import in.prismar.game.web.route.GetWebRoute;
import in.prismar.game.web.route.PostWebRoute;
import io.javalin.http.Context;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        super("banner/{player}");
        this.game = game;
        this.server = server;
    }

    @Override
    public BannerResponse onRoute(Context context) {
        final String id = context.pathParam("player");
        Player player = Bukkit.getPlayer(id);
        if(player == null) {
            return new BannerResponse(false);
        }
        Banner banner = server.getGson().fromJson(context.body(), Banner.class);
        Bukkit.getScheduler().runTask(game, () -> {
            ItemStack stack = new ItemStack(Material.valueOf(banner.getBaseColor().name() + "_BANNER"));
            BannerMeta meta = (BannerMeta) stack.getItemMeta();
            List<Pattern> patterns = new ArrayList<>();
            for(BannerPattern pattern : banner.getPatterns()) {
                patterns.add(new Pattern(pattern.getColor(), pattern.getPattern()));
            }
            meta.setPatterns(patterns);
            stack.setItemMeta(meta);

            player.getInventory().addItem(stack);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have received a §abanner");
        });
        return new BannerResponse(true);
    }
}
