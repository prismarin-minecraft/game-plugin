package in.prismar.game.item.frame;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.model.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.model.Skin;
import in.prismar.game.item.model.SkinableItem;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.event.FrameClickEvent;
import in.prismar.library.spigot.inventory.event.FrameCloseEvent;
import in.prismar.library.spigot.item.CustomSkullBuilder;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@Getter
public class SkinsFrame extends Frame {

    private static final ItemStack BACK = new ItemBuilder(Material.OAK_DOOR).setName("§cBack").build();
    private static final ItemStack ARROW_RIGHT = new CustomSkullBuilder("https://textures.minecraft.net/texture/682ad1b9cb4dd21259c0d75aa315ff389c3cef752be3949338164bac84a96e").build();
    private static final int[] SLOTS = {4, 5, 6, 7,
                                        13, 14, 15, 16,
                                        22, 23, 24, 25};

    private static final ItemStack AIR = new ItemStack(Material.AIR);

    private ItemStack item;
    private SkinableItem skinableItem;

    @Setter
    private boolean receiveBack = true;

    public SkinsFrame(CustomItemRegistry registry, ItemStack item) {
        super("§cSkins Table", 3);
        this.item = item;
        fill();

        addButton(18, BACK, (ClickFrameButtonEvent) (player, event) -> {
            ModificationsFrame frame = new ModificationsFrame(registry);
            frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.7f);
        });

        addButton(10, createArrowRightItem("§bGun"));

        if(item != null) {
            CustomItem customItem = registry.getItemByStack(item);
            if(customItem instanceof SkinableItem skinableItem) {
                this.skinableItem = skinableItem;
                addButton(11, item, (ClickFrameButtonEvent) (player, inventoryClickEvent) -> {
                    SkinsFrame frame = new SkinsFrame(registry, null);
                    frame.openInventory(player, Sound.BLOCK_PISTON_CONTRACT, 0.7F);
                });
                for (int i = 0; i < SLOTS.length; i++) {
                    int slot = SLOTS[i];
                    if(skinableItem.getSkins().size() > i) {
                        Skin skin = skinableItem.getSkins().get(i);
                        ItemStack stack = item.clone();
                        ItemMeta meta = stack.getItemMeta();
                        meta.setDisplayName(skin.getDisplayName());
                        meta.setCustomModelData(skin.getData());
                        stack.setItemMeta(meta);
                        addButton(slot, stack, (ClickFrameButtonEvent) (player, event) -> {
                            //TODO: Permissions
                            ItemStack skinItem = item.clone();
                            ItemMeta skinItemItemMeta = skinItem.getItemMeta();
                            skinItemItemMeta.setCustomModelData(skin.getData());
                            skinItem.setItemMeta(skinItemItemMeta);
                            setReceiveBack(false);
                            SkinsFrame frame = new SkinsFrame(registry, skinItem);
                            frame.openInventory(player, Sound.BLOCK_ANVIL_STEP, 0.5f);
                        });
                    } else {
                        addButton(slot, AIR);
                    }
                }

            }
        } else {
            addButton(11, AIR);
        }

        getEventBus().subscribe(FrameClickEvent.class, event -> {
            if(event.getEvent().getClickedInventory() != null) {
                if(event.getEvent().getClickedInventory().getType() == InventoryType.PLAYER) {
                    event.getEvent().setCancelled(true);
                    event.getEvent().setResult(Event.Result.DENY);
                    ItemStack stack = event.getEvent().getCurrentItem();
                    CustomItem customItem = registry.getItemByStack(stack);
                    if(customItem != null) {
                        if(customItem instanceof SkinableItem) {
                            if(this.item != null) {
                                ItemUtil.giveItem(event.getPlayer(), item);
                            }
                            setReceiveBack(false);
                            SkinsFrame frame = new SkinsFrame(registry, event.getEvent().getCurrentItem());
                            frame.openInventory(event.getPlayer(), Sound.BLOCK_PISTON_EXTEND, 0.7f);
                            event.getEvent().setCurrentItem(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        });

        getEventBus().subscribe(FrameCloseEvent.class, event -> {
            if(item != null && receiveBack) {
                event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_ANVIL_USE, 0.7f, 0.7f);
                ItemUtil.giveItem(event.getPlayer(), item);
            }
        });


        build();
    }

    private ItemStack createArrowRightItem(String display) {
        ItemStack stack = ARROW_RIGHT.clone();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(display);
        stack.setItemMeta(meta);
        return stack;
    }
}
