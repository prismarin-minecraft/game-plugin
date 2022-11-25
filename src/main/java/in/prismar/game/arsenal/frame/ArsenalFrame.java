package in.prismar.game.arsenal.frame;

import in.prismar.api.user.User;
import in.prismar.api.user.data.ArsenalItem;
import in.prismar.game.arsenal.ArsenalService;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.type.GunType;
import in.prismar.library.common.event.EventSubscriber;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.event.FrameClickEvent;
import in.prismar.library.spigot.item.CustomSkullBuilder;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ArsenalFrame extends Frame implements EventSubscriber<FrameClickEvent> {

    private static final ItemStack ARROW_DOWN = new CustomSkullBuilder("https://textures.minecraft.net/texture/72431911f4178b4d2b413aa7f5c78ae4447fe9246943c31df31163c0e043e0d6").build();
    private static final ItemStack ARROW_RIGHT = new CustomSkullBuilder("https://textures.minecraft.net/texture/682ad1b9cb4dd21259c0d75aa315ff389c3cef752be3949338164bac84a96e").build();

    private static final int PRIMARY_SLOT = 22;
    private static final int SECONDARY_SLOT = 24;
    private static final int HELMET_SLOT = 11;
    private static final int CHESTPLATE_SLOT = 20;
    private static final int LEGGINGS_SLOT = 29;
    private static final int BOOTS_SLOT = 38;

    private final ArsenalService service;
    private Player player;
    private User user;

    public ArsenalFrame(Player player, ArsenalService service) {
        super("§6Arsenal", 6);
        this.service = service;
        this.player = player;
        this.user = service.manage(player);

        this.getEventBus().subscribe(FrameClickEvent.class, this);
        fill();

        addButton(PRIMARY_SLOT-9, createArrowDownItem("§cPrimary"));
        addButton(SECONDARY_SLOT-9, createArrowDownItem("§eSecondary"));

        addButton(HELMET_SLOT-1, createArrowRightItem("§dHelmet"));
        addButton(CHESTPLATE_SLOT-1, createArrowRightItem("§dChestplate"));
        addButton(LEGGINGS_SLOT-1, createArrowRightItem("§dLeggings"));
        addButton(BOOTS_SLOT-1, createArrowRightItem("§dBoots"));

        addArsenalItem(PRIMARY_SLOT, "primary", "§cEmpty");
        addArsenalItem(SECONDARY_SLOT, "secondary", "§cEmpty");

        addArsenalItem(HELMET_SLOT, "helmet", "§cEmpty");
        addArsenalItem(CHESTPLATE_SLOT, "chestplate", "§cEmpty");
        addArsenalItem(LEGGINGS_SLOT, "leggings", "§cEmpty");
        addArsenalItem(BOOTS_SLOT, "boots", "§cEmpty");



        build();
    }

    private ItemStack createArrowDownItem(String display) {
        ItemStack stack = ARROW_DOWN.clone();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(display);
        stack.setItemMeta(meta);
        return stack;
    }

    private ItemStack createArrowRightItem(String display) {
        ItemStack stack = ARROW_RIGHT.clone();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(display);
        stack.setItemMeta(meta);
        return stack;
    }

    private void addArsenalItem(int slot, String key, String placeholderDisplay) {
        ArsenalItem item = service.getItem(user, key);
        addButton(slot, item == null ? new ItemBuilder(Material.IRON_BARS).setName(placeholderDisplay).build() : item.getItem());
    }

    @Override
    public void onEvent(FrameClickEvent event) {
        if(event.getEvent().getClickedInventory() != null) {
            if(event.getEvent().getClickedInventory().getType() == InventoryType.PLAYER) {
                ItemStack stack = event.getEvent().getCurrentItem();
                CustomItem item = service.getItemRegistry().getItemByStack(stack);
                if(item != null) {
                    if(item instanceof Gun gun) {
                        if(gun.getType() == GunType.PISTOL) {
                            service.setItem(user, "secondary", stack.clone());
                            reopen();
                        } else {
                            service.setItem(user, "primary", stack.clone());
                            reopen();
                        }
                    }
                }
            }
        }
    }

    public void reopen() {
        ArsenalFrame frame = new ArsenalFrame(player, service);
        frame.openInventory(player, Sound.BLOCK_PISTON_CONTRACT, 0.7f);
    }
}
