package in.prismar.game.arsenal;

import in.prismar.api.PrismarinApi;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.api.user.data.ArsenalItem;
import in.prismar.api.user.data.SeasonData;
import in.prismar.game.Game;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.attachment.AttachmentModifier;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.type.AmmoType;
import in.prismar.game.item.model.CustomItem;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import in.prismar.library.spigot.serializer.BukkitObjectSerializer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

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
    private Game game;

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
        for(Map.Entry<String, ArsenalItem> entry : data.getArsenal().entrySet()) {
            if(!entry.getKey().equals("lethal")) {
                ArsenalItem item = entry.getValue();
                if(item.getItem() == null) {
                    item.setItem((ItemStack) BukkitObjectSerializer.getSerializer().deserialize(item.getValue()));
                }
            } else {
                ArsenalItem item = entry.getValue();
                if(item.getItem() == null) {
                    item.setItem(itemRegistry.createItem(item.getValue()));
                }
            }
        }
        return user;
    }

    public void fillAmmo(Player player) {
        for (int i = 9; i < 12; i++) {
            ItemStack item = AmmoType.AR.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 12; i < 13; i++) {
            ItemStack item = AmmoType.SNIPER.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 13; i < 14; i++) {
            ItemStack item = AmmoType.SHOTGUN.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 14; i < 17; i++) {
            ItemStack item = AmmoType.SMG.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
        for (int i = 17; i < 18; i++) {
            ItemStack item = AmmoType.PISTOL.getItem().clone();
            item.setAmount(64);
            player.getInventory().setItem(i, item);
        }
    }

    public void giveHotbarLoadout(Player player) {
        User user = manage(player);
        ItemStack primary = createArsenalItem(user, "primary");
        if (primary != null) {
            player.getInventory().setItem(0, primary);
        }
        ItemStack secondary = createArsenalItem(user, "secondary");
        if (secondary != null) {
            player.getInventory().setItem(1, secondary);
        }
        ItemStack lethal = createArsenalItem(user, "lethal");
        if(lethal == null) {
            player.getInventory().setItem(3, itemRegistry.createItem("Grenade"));
        } else {
            player.getInventory().setItem(3, lethal.clone());
        }
    }

    public void giveLoadout(Player player) {
        User user = manage(player);


        ItemStack helmet = createArsenalItem(user, "helmet");
        if (helmet != null) {
            player.getInventory().setHelmet(helmet);
        }
        giveArsenalChestplate(user, player);


        ItemStack leggings = createArsenalItem(user, "leggings");
        if (leggings != null) {
            player.getInventory().setLeggings(leggings);
        }

        ItemStack boots = createArsenalItem(user, "boots");
        if (boots != null) {
            player.getInventory().setBoots(boots);
        }

        giveHotbarLoadout(player);

    }

    public boolean giveArsenalChestplate(User user, Player player) {
        ItemStack chestplate = createArsenalItem(user, "chestplate");
        if (chestplate != null) {
            player.getInventory().setChestplate(chestplate);
            return true;
        }
        return false;
    }

    private ItemStack createArsenalItem(User user, String key) {
        ArsenalItem item = getItem(user, key);
        if (item != null) {
            ItemStack stack = item.getItem().clone();
            CustomItem customItem = itemRegistry.getItemByStack(stack);
            if (customItem != null) {
                if(customItem instanceof Gun gun) {
                    int maxAmmo = gun.getMaxAmmo();
                    for(Attachment attachment : gun.getAttachments(game, stack, true)) {
                        maxAmmo = attachment.apply(AttachmentModifier.MAX_AMMO, maxAmmo);
                    }
                    PersistentItemDataUtil.setInteger(game, stack, Gun.AMMO_KEY, maxAmmo);
                }
            }
            return stack;
        }
        return null;
    }


    public void giveStarterArsenal(User user) {
        setItem(user, "helmet", itemRegistry.createItem("RecruitHelmet"));
        setItem(user, "chestplate", itemRegistry.createItem("RecruitChestplate"));
        setItem(user, "leggings", itemRegistry.createItem("RecruitLeggings"));
        setItem(user, "boots", itemRegistry.createItem("RecruitBoots"));

        setItem(user, "primary", itemRegistry.createItem("ump45"));
        setItem(user, "secondary", itemRegistry.createItem("glock17"));

        setItem(user, "lethal", itemRegistry.createItem("Grenade"), "Grenade");
    }

    public void setItem(User user, String key, ItemStack item) {
        setItem(user, key, item, BukkitObjectSerializer.getSerializer().serialize(item));
    }

    public void setItem(User user, String key, ItemStack item, String value) {
        SeasonData data = user.getSeasonData();
        ArsenalItem arsenalItem = new ArsenalItem();
        arsenalItem.setItem(item);
        arsenalItem.setValue(value);
        data.getArsenal().put(key.toLowerCase(), arsenalItem);
        provider.saveAsync(user, true);
    }

    @Nullable
    public ArsenalItem getItem(User user, String key) {
        SeasonData data = user.getSeasonData();
        return data.getArsenal().get(key.toLowerCase());
    }

}
