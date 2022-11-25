package in.prismar.game.arsenal;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.ArsenalItem;
import in.prismar.api.user.data.SeasonData;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.serializer.BukkitObjectSerializer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service
public class ArsenalService {

    @Inject
    private CustomItemRegistry itemRegistry;

    private final UserProvider<User> provider;

    public ArsenalService() {
        this.provider = PrismarinApi.getProvider(UserProvider.class);
    }

    public User manage(Player player) {
        User user = provider.getUserByUUID(player.getUniqueId());
        SeasonData data = user.getSeasonData();
        if(data.getArsenal() == null) {
            data.setArsenal(new HashMap<>());
        }
        for(ArsenalItem item : data.getArsenal().values()) {
            if(item.getItem() == null) {
                item.setItem((ItemStack) BukkitObjectSerializer.getSerializer().deserialize(item.getValue()));
            }
        }
        return user;
    }

    public void setItem(User user, String key, ItemStack item) {
        SeasonData data = user.getSeasonData();
        ArsenalItem arsenalItem = new ArsenalItem();
        arsenalItem.setItem(item);
        arsenalItem.setValue(BukkitObjectSerializer.getSerializer().serialize(item));
        data.getArsenal().put(key.toLowerCase(), arsenalItem);
        provider.saveAsync(user, true);
    }

    @Nullable
    public ArsenalItem getItem(User user, String key) {
        SeasonData data = user.getSeasonData();
        return data.getArsenal().get(key.toLowerCase());
    }

}
