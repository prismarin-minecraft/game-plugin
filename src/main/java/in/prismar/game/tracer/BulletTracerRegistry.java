package in.prismar.game.tracer;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.tracer.impl.ColoredBulletTracer;
import in.prismar.game.tracer.impl.RainbowBulletTracer;
import in.prismar.library.common.registry.LocalMapRegistry;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Service
@Getter
public class BulletTracerRegistry extends LocalMapRegistry<String, BulletTracer> {

    private final UserProvider<User> userProvider;

    public BulletTracerRegistry() {
        super(false, false);
        this.userProvider = PrismarinApi.getProvider(UserProvider.class);


        register("scouter", new ColoredBulletTracer(new ItemBuilder(Material.GREEN_WOOL).setName("§2Scouter").build(), Color.GREEN, 1));
        register("runner", new ColoredBulletTracer(new ItemBuilder(Material.ORANGE_WOOL).setName("§6Runner").build(), Color.ORANGE, 1));
        register("rogue", new ColoredBulletTracer(new ItemBuilder(Material.BLUE_WOOL).setName("§9Rogue").build(), Color.BLUE, 1));
        register("predator", new ColoredBulletTracer(new ItemBuilder(Material.RED_WOOL).setName("§4Predator").build(), Color.RED, 1));
        register("pink", new ColoredBulletTracer(new ItemBuilder(Material.PINK_WOOL).setName("§dPink").build(), Color.FUCHSIA, 1));
        register("rainbow", new RainbowBulletTracer());
    }

    public void removeTracer(User user) {
        user.getData().getAttachments().remove("bullet.tracer");
    }

    public void setTracer(User user, String id) {
        user.getData().getAttachments().put("bullet.tracer", id);
    }

    public boolean hasTracer(Player player, String id) {
        return player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "tracers." + id);
    }

    public BulletTracer getByUser(User user) {
        if(user.getData().getAttachments().containsKey("bullet.tracer")) {
            final String id = (String) user.getData().getAttachments().get("bullet.tracer");
            if(existsById(id)) {
                return getById(id);
            }
        }
        return null;
    }
}
