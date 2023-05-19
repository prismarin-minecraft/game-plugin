package in.prismar.game.item;

import in.prismar.game.Game;
import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
import in.prismar.game.item.impl.deployable.SandbagItem;
import in.prismar.game.item.impl.misc.FFALeaveItem;
import in.prismar.game.item.impl.misc.HardpointLeaveItem;
import in.prismar.game.item.impl.misc.RailgunItem;
import in.prismar.game.item.impl.misc.UAVItem;
import in.prismar.game.item.impl.armor.hardpoint.HardpointBoots;
import in.prismar.game.item.impl.armor.hardpoint.HardpointChestplate;
import in.prismar.game.item.impl.armor.hardpoint.HardpointHelmet;
import in.prismar.game.item.impl.armor.hardpoint.HardpointLeggings;
import in.prismar.game.item.impl.armor.heavy.HeavyBoots;
import in.prismar.game.item.impl.armor.heavy.HeavyChestplate;
import in.prismar.game.item.impl.armor.heavy.HeavyHelmet;
import in.prismar.game.item.impl.armor.heavy.HeavyLeggings;
import in.prismar.game.item.impl.armor.juggernaut.JuggernautBoots;
import in.prismar.game.item.impl.armor.juggernaut.JuggernautChestplate;
import in.prismar.game.item.impl.armor.juggernaut.JuggernautHelmet;
import in.prismar.game.item.impl.armor.juggernaut.JuggernautLeggings;
import in.prismar.game.item.impl.armor.lightweight.LightweightBoots;
import in.prismar.game.item.impl.armor.lightweight.LightweightChestplate;
import in.prismar.game.item.impl.armor.lightweight.LightweightHelmet;
import in.prismar.game.item.impl.armor.lightweight.LightweightLeggings;
import in.prismar.game.item.impl.armor.recruit.RecruitBoots;
import in.prismar.game.item.impl.armor.recruit.RecruitChestplate;
import in.prismar.game.item.impl.armor.recruit.RecruitHelmet;
import in.prismar.game.item.impl.armor.recruit.RecruitLeggings;
import in.prismar.game.item.impl.attachment.impl.*;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.medical.BandageItem;
import in.prismar.game.item.impl.medical.MedicalSyringeItem;
import in.prismar.game.item.impl.medical.MedkitItem;
import in.prismar.game.item.impl.placeable.LandmineCustomItem;
import in.prismar.game.item.impl.throwable.*;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.reader.CustomItemReader;
import in.prismar.library.common.event.EventBus;
import in.prismar.library.meta.anno.Service;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
@Service
public class CustomItemRegistry {

    private final Game game;

    private CustomItemReader reader;
    private Map<String, CustomItem> items;
    private Map<UUID, List<CustomItemHolder>> holders;

    private EventBus eventBus;

    public CustomItemRegistry(Game game) {
        this.game = game;
        this.eventBus = new EventBus();
        this.items = new LinkedHashMap<>();
        this.reader = new CustomItemReader();
        this.holders = new HashMap<>();
        load();

        Bukkit.getScheduler().runTaskTimer(game, new CustomItemUpdater(game, this), 1, 1);
    }

    public void load() {
        this.items.clear();




        //register(new TestGun());

        register(new GrenadeItem());
        register(new MolotovItem());
        register(new GasGrenadeItem());
        register(new ImpactGrenadeItem());
        register(new UAVItem());

        register(new AdaptiveChamberingAttachment());
        register(new VerticalGripAttachmentItem());
        register(new HorizontalGripAttachmentItem());
        register(new BipodAttachmentItem());
        register(new ExtendedMagazineAttachmentItem());
        register(new BarrelAttachmentItem());
        register(new SuppressorAttachmentItem());


        register(new MedicalSyringeItem());
        register(new BandageItem());
        register(new MedkitItem());

        register(new AirstrikeItem());
        register(new AirdropItem());

        register(new FFALeaveItem());
        register(new HardpointLeaveItem());

        register(new RecruitHelmet());
        register(new RecruitChestplate());
        register(new RecruitLeggings());
        register(new RecruitBoots());

        register(new LightweightHelmet());
        register(new LightweightChestplate());
        register(new LightweightLeggings());
        register(new LightweightBoots());

        register(new HeavyHelmet());
        register(new HeavyChestplate());
        register(new HeavyLeggings());
        register(new HeavyBoots());

        register(new JuggernautHelmet());
        register(new JuggernautChestplate());
        register(new JuggernautLeggings());
        register(new JuggernautBoots());

        register(new RailgunItem());

        register(new LandmineCustomItem());

        register(new SandbagItem());


        for(HardpointTeam team : HardpointTeam.values()) {
            register(new HardpointHelmet(team.getFancyName(), team.getColor()));
            register(new HardpointChestplate(team.getFancyName(), team.getColor()));
            register(new HardpointLeggings(team.getFancyName(), team.getColor()));
            register(new HardpointBoots(team.getFancyName(), team.getColor()));
        }

        reader.apply(this);

    }




    public List<CustomItemHolder> publishEvent(Player player, Object event) {
        if (holders.containsKey(player.getUniqueId())) {
            List<CustomItemHolder> list = holders.get(player.getUniqueId());
            for (CustomItemHolder holder : list) {
                holder.getItem().getEventBus().publish(player, game, holder, event);
            }
            return list;
        }
        return Collections.emptyList();
    }

    public List<CustomItemHolder> publishEvent(Player player, Class<?> eventClass, Object event) {
        if (holders.containsKey(player.getUniqueId())) {
            List<CustomItemHolder> list = holders.get(player.getUniqueId());
            for (CustomItemHolder holder : list) {
                holder.getItem().getEventBus().publish(player, game, holder, eventClass, event);
            }
            return list;
        }
        return Collections.emptyList();
    }

    public List<CustomItemHolder> scan(Player player) {
        List<CustomItemHolder> items = new ArrayList<>();

        for (ItemStack stack : player.getInventory().getStorageContents()) {
            CustomItem item = getItemByStack(stack);
            if (item != null) {
                if (!isItemInHand(player, stack, true) && !isItemInHand(player, stack, false)) {
                    items.add(new CustomItemHolder(item, stack, CustomItemHoldingType.INVENTORY));
                }
            }
        }
        ItemStack rightHandItem = player.getInventory().getItemInMainHand();
        CustomItem rightHandCustomItem = getItemByStack(rightHandItem);
        if (rightHandCustomItem != null) {
            items.add(new CustomItemHolder(rightHandCustomItem, rightHandItem, CustomItemHoldingType.RIGHT_HAND));
        }

        ItemStack leftHandItem = player.getInventory().getItemInOffHand();
        CustomItem leftHandCustomItem = getItemByStack(leftHandItem);
        if (leftHandCustomItem != null) {
            items.add(new CustomItemHolder(leftHandCustomItem, leftHandItem, CustomItemHoldingType.LEFT_HAND));
        }


        for (ItemStack stack : player.getInventory().getArmorContents()) {
            CustomItem item = getItemByStack(stack);
            if (item != null) {
                items.add(new CustomItemHolder(item, stack, CustomItemHoldingType.ARMOR));
            }
        }
        holders.put(player.getUniqueId(), items);
        return items;
    }

    private boolean isItemInHand(Player player, ItemStack stack, boolean right) {
        ItemStack itemStack = right ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        if (itemStack != null) {
            if (itemStack.hasItemMeta()) {
                if (itemStack.getItemMeta().hasDisplayName()) {
                    return itemStack.getItemMeta().getDisplayName().equals(stack.getItemMeta().getDisplayName());
                }
            }
        }
        return false;
    }

    public void register(CustomItem item) {
        this.items.put(item.getId().toLowerCase(), item);
    }

    public CustomItem getItemById(String id) {
        return this.items.get(id.toLowerCase());
    }

    public boolean existsItemById(String id) {
        return this.items.containsKey(id.toLowerCase());
    }

    public boolean existsItemByIdIgnoreCase(String id) {
        for (CustomItem item : items.values()) {
            if (item.getId().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    public CustomItem getItemByDisplayName(String displayName) {
        for (CustomItem customItem : getItems().values()) {
            if (customItem.getDisplayName().equals(displayName)) {
                return customItem;
            }
        }
        return null;
    }

    public CustomItem getItemByStack(ItemStack stack) {
        if (stack != null) {
            if (stack.hasItemMeta()) {
                if (stack.getItemMeta().hasDisplayName()) {
                    return getItemByDisplayName(stack.getItemMeta().getDisplayName());
                }
            }
        }
        return null;
    }

    public ItemStack createItem(String id) {
        return getItemById(id.toLowerCase()).build();
    }

    public ItemStack createGunItem(String id) {
        CustomItem customItem = getItemById(id.toLowerCase());
        if (customItem instanceof Gun gun) {

            ItemStack item = getItemById(id.toLowerCase()).build();

            PersistentItemDataUtil.setInteger(game, item, Gun.AMMO_KEY, gun.getMaxAmmo());
            return item;
        }
        return customItem.build();

    }
}
