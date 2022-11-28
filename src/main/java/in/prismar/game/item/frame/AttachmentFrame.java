package in.prismar.game.item.frame;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.impl.attachment.Attachment;
import in.prismar.game.item.impl.gun.Gun;
import in.prismar.game.item.impl.gun.type.GunType;
import in.prismar.library.common.event.EventSubscriber;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.event.FrameClickEvent;
import in.prismar.library.spigot.inventory.event.FrameCloseEvent;
import in.prismar.library.spigot.item.CustomSkullBuilder;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.item.PersistentItemDataUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
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
public class AttachmentFrame extends Frame {

    private static final ItemStack ARROW_RIGHT = new CustomSkullBuilder("https://textures.minecraft.net/texture/682ad1b9cb4dd21259c0d75aa315ff389c3cef752be3949338164bac84a96e").build();
    private static final ItemStack BLOCKED_ATTACHMENT_SLOT = new ItemBuilder(Material.IRON_BARS).setName("§cBlocked Slot")
            .addLore(" " + PrismarinConstants.ARROW_RIGHT + "§7You can buy this slot at our store§8: §b/store")
            .build();
    private static final int[] SLOTS = {29, 30, 31, 32, 33};

    private ItemStack item;
    private Gun gun;

    @Setter
    private boolean receiveBack = true;

    public AttachmentFrame(CustomItemRegistry registry, ItemStack item) {
        super("§cAttachments Table", 5);
        this.item = item;
        fill();

        addButton(12, createArrowRightItem("§bGun"));

        if(item != null) {
            CustomItem customItem = registry.getItemByStack(item);
            if(customItem instanceof Gun gun) {
                this.gun = gun;
                addButton(13, item, (ClickFrameButtonEvent) (player, inventoryClickEvent) -> {
                    AttachmentFrame frame = new AttachmentFrame(registry, null);
                    frame.openInventory(player, Sound.BLOCK_PISTON_CONTRACT, 0.7F);
                });

                List<Attachment> attachments = gun.getAttachments(registry.getGame(), item);
                for (int i = 0; i < SLOTS.length; i++) {
                    if(i+1 <= gun.getAttachmentSlots()) {
                        if(i+1 <= attachments.size()) {
                            Attachment attachment = attachments.get(i);
                            addButton(SLOTS[i], attachment.build(), (ClickFrameButtonEvent) (player, event) -> {
                                attachments.remove(attachment);
                                PersistentItemDataUtil.setString(registry.getGame(), item, Gun.ATTACHMENTS_KEY, Joiner.on(",").join(attachments));
                                ItemUtil.giveItem(player, attachment.build());
                                rebuildLore(attachments);
                                setReceiveBack(false);
                                AttachmentFrame frame = new AttachmentFrame(registry, item);
                                frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.7F);
                            });
                        } else {
                            addButton(SLOTS[i], new ItemStack(Material.AIR));
                        }
                    } else {
                        addButton(SLOTS[i], BLOCKED_ATTACHMENT_SLOT);
                    }
                }
            }
        } else {
            addButton(13, new ItemStack(Material.AIR));
        }

        getEventBus().subscribe(FrameClickEvent.class, event -> {
            if(event.getEvent().getClickedInventory() != null) {
                if(event.getEvent().getClickedInventory().getType() == InventoryType.PLAYER) {
                    event.getEvent().setCancelled(true);
                    event.getEvent().setResult(Event.Result.DENY);
                    ItemStack stack = event.getEvent().getCurrentItem();
                    CustomItem customItem = registry.getItemByStack(stack);
                    if(customItem != null) {
                        if(customItem instanceof Gun gun) {
                            setReceiveBack(false);
                            AttachmentFrame frame = new AttachmentFrame(registry, event.getEvent().getCurrentItem());
                            frame.openInventory(event.getPlayer(), Sound.BLOCK_PISTON_EXTEND, 0.7f);
                            event.getEvent().setCurrentItem(new ItemStack(Material.AIR));
                        } else if(customItem instanceof Attachment attachment) {
                            if(gun != null) {
                                List<Attachment> attachments = gun.getAttachments(registry.getGame(), item);
                                if(attachments.size() >= gun.getAttachmentSlots()) {
                                    return;
                                }
                                if(!attachment.isAllowedToAttach(gun, attachments)) {
                                    return;
                                }
                                attachments.add(attachment);
                                PersistentItemDataUtil.setString(registry.getGame(), item, Gun.ATTACHMENTS_KEY, Joiner.on(",").join(attachments));
                                event.getEvent().setCurrentItem(new ItemStack(Material.AIR));
                                setReceiveBack(false);
                                rebuildLore(attachments);
                                AttachmentFrame frame = new AttachmentFrame(registry, item);
                                frame.openInventory(event.getPlayer(), Sound.UI_BUTTON_CLICK, 0.7F);
                            }
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

    private void rebuildLore(List<Attachment> attachments) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(gun.buildDefaultLore(attachments));
        if(attachments.isEmpty()) {
            if(meta.hasEnchants()) {
                for(Enchantment enchantment : meta.getEnchants().keySet()) {
                    meta.removeEnchant(enchantment);
                }
            }
        } else {
            meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        }
        item.setItemMeta(meta);
    }

    private ItemStack createArrowRightItem(String display) {
        ItemStack stack = ARROW_RIGHT.clone();
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(display);
        stack.setItemMeta(meta);
        return stack;
    }
}
