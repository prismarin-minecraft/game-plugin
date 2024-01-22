package in.prismar.game.item;

import in.prismar.api.configuration.node.event.ConfigRefreshEvent;
import in.prismar.api.item.CustomItemProvider;
import in.prismar.api.item.TeaType;
import in.prismar.game.Game;
import in.prismar.game.hardpoint.HardpointTeam;
import in.prismar.game.item.holder.CustomItemHolder;
import in.prismar.game.item.holder.CustomItemHoldingType;
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
import in.prismar.game.item.impl.armor.misc.GasMaskHelmet;
import in.prismar.game.item.impl.armor.recruit.RecruitBoots;
import in.prismar.game.item.impl.armor.recruit.RecruitChestplate;
import in.prismar.game.item.impl.armor.recruit.RecruitHelmet;
import in.prismar.game.item.impl.armor.recruit.RecruitLeggings;
import in.prismar.game.item.impl.attachment.impl.*;
import in.prismar.game.item.impl.backpack.SmallBackpackItem;
import in.prismar.game.item.impl.deployable.SandbagItem;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.impl.GrenadeLauncherItem;
import in.prismar.game.item.impl.gun.impl.Railgun;
import in.prismar.game.item.impl.medical.BandageItem;
import in.prismar.game.item.impl.medical.MedicalSyringeItem;
import in.prismar.game.item.impl.medical.MedkitItem;
import in.prismar.game.item.impl.misc.*;
import in.prismar.game.item.impl.placeable.LandmineCustomItem;
import in.prismar.game.item.impl.tea.TeaItem;
import in.prismar.game.item.impl.throwable.*;
import in.prismar.game.item.impl.tools.FastAxeItem;
import in.prismar.game.item.impl.tools.FastPickaxeItem;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.reader.CustomItemReader;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.meta.anno.Service;
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
public class CustomItemRegistry implements CustomItemProvider {

    private final Game game;

    private final CustomItemReader reader;
    private final Map<String, CustomItem> items;
    private final Map<UUID, List<CustomItemHolder>> holders;
    private final List<CustomItemAmmoTempCacheChecker> ammoTempCacheCheckers;


    public CustomItemRegistry(Game game) {
        this.game = game;
        this.items = new LinkedHashMap<>();
        this.reader = new CustomItemReader(game);
        this.holders = new HashMap<>();
        this.ammoTempCacheCheckers = new ArrayList<>();


        load();

        Bukkit.getScheduler().runTaskTimer(game, new CustomItemUpdater(game, this), 1, 1);
    }

    public void load() {
        this.items.clear();




      //  register(new TestGun());

        register(new GrenadeItem());
        register(new MolotovItem());
        register(new GasGrenadeItem());
        register(new ImpactGrenadeItem());
        register(new UAVItem());
        register(new Railgun());

        register(new AdaptiveChamberingAttachment());
        register(new VerticalGripAttachmentItem());
        register(new HorizontalGripAttachmentItem());
        register(new BipodAttachmentItem());
        register(new ExtendedMagazineAttachmentItem());
        register(new BarrelAttachmentItem());
        register(new SuppressorAttachmentItem());
        register(new LaserAttachmentItem());


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

        register(new GasMaskHelmet());

        register(new HeavyHelmet());
        register(new HeavyChestplate());
        register(new HeavyLeggings());
        register(new HeavyBoots());

        register(new JuggernautHelmet());
        register(new JuggernautChestplate());
        register(new JuggernautLeggings());
        register(new JuggernautBoots());

        register(new RaygunItem());

        register(new LandmineCustomItem());

        register(new SandbagItem());

       // register(new GrapplingGunItem());

        register(new RiotShieldItem());
        register(new GrenadeLauncherItem());

        register(new FlashbangItem());

        register(new PortableMinerItem());
        register(new PortableBuilderItem());

        register(new FastAxeItem());
        register(new FastPickaxeItem());


        for(HardpointTeam team : HardpointTeam.values()) {
            register(new HardpointHelmet(team.getFancyName(), team.getColor()));
            register(new HardpointChestplate(team.getFancyName(), team.getColor()));
            register(new HardpointLeggings(team.getFancyName(), team.getColor()));
            register(new HardpointBoots(team.getFancyName(), team.getColor()));
        }


        register(new TeaItem(TeaType.MINER, "§7Miner's Tea §6[Tier 1]", 1, 1));
        register(new TeaItem(TeaType.MINER, "§7Miner's Tea §6[Tier 2]", 2, 2));
        register(new TeaItem(TeaType.MINER, "§7Miner's Tea §6[Tier 3]", 3, 3));

        register(new TeaItem(TeaType.LUMBERJACK, "§2Lumberjack's Tea §6[Tier 1]", 4, 1));
        register(new TeaItem(TeaType.LUMBERJACK, "§2Lumberjack's Tea §6[Tier 2]", 5, 2));
        register(new TeaItem(TeaType.LUMBERJACK, "§2Lumberjack's Tea §6[Tier 3]", 6, 3));

        register(new TeaItem(TeaType.FARMER, "§aFarmer's Tea §6[Tier 1]", 7, 1));
        register(new TeaItem(TeaType.FARMER, "§aFarmer's Tea §6[Tier 2]", 8, 2));
        register(new TeaItem(TeaType.FARMER, "§aFarmer's Tea §6[Tier 3]", 9, 3));


        register(new SmallBackpackItem());

    }

    @SafeInitialize
    private void initialize() {
        reader.load();
        reader.apply(this);

        game.getConfigNodeFile().getEventBus().subscribe(ConfigRefreshEvent.class, event -> {
            load();
            reader.apply(this);
            game.getTeaService().load(game.getItemRegistry());
        });

        game.getTeaService().load(this);
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
        if(!existsItemById(id)) {
            return null;
        }
        CustomItem item = getItemById(id.toLowerCase());
        ItemStack stack = item.build();
        return stack;
    }

    @Override
    public void createAmmoFilledGun(Player player, ItemStack based) {
        CustomItem customItem = getItemByStack(based);
        if(customItem != null) {
            if(customItem instanceof Gun gun) {
                game.getItemAmmoProvider().setAmmo(player, based, gun.getMaxAmmo());
            }
        }
    }

    @Override
    public void addCustomItemAmmoTempCacheChecker(CustomItemAmmoTempCacheChecker checker) {
        this.ammoTempCacheCheckers.add(checker);
    }
}
