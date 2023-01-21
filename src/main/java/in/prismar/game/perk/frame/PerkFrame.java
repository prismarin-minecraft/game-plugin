package in.prismar.game.perk.frame;

import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.game.perk.Perk;
import in.prismar.game.perk.PerkService;
import in.prismar.library.common.math.NumberFormatter;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class PerkFrame extends Frame {

    private final PerkService service;
    private final Player player;

    public PerkFrame(PerkService service, Player player) {
        super("§9Perks", 3);
        this.service = service;
        this.player = player;
        fill();

        addButton(26, new ItemBuilder(Material.OAK_DOOR).setName("§cBack to loadout").build(), (ClickFrameButtonEvent) (player12, event) -> player12.performCommand("loadout"));


        int slot = 11;
        for (Perk perk : Perk.values()) {
            ItemBuilder builder = new ItemBuilder(perk.getIcon()).addLore("§c");
            if (service.hasPerk(player, perk)) {
                builder.glow();
                builder.addLore("§aActive");
            } else {
                if (service.ownPerk(player, perk)) {
                    builder.addLore("§7Click me to activate this perk");
                } else {
                    if (perk.getBuyPrice() > 0) {
                        builder.addLore("§7Click me to buy this perk for §6" + NumberFormatter.formatDoubleToThousands(perk.getBuyPrice()) + " $");
                    } else {
                        builder.addLore("§cYou do not own this perk");
                    }
                }

            }

            addButton(slot, builder.build(), (ClickFrameButtonEvent) (player1, event) -> {
                if (service.ownPerk(player, perk)) {
                    if (service.hasPerk(player, perk)) {
                        return;
                    }
                    service.setPerk(player, perk);
                    reopen();
                    return;
                }
                if (perk.getBuyPrice() > 0) {
                    User user = service.getUserProvider().getUserByUUID(player.getUniqueId());
                    if(user.getSeasonData().getBalance() < perk.getBuyPrice()) {
                        player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 0.3f, 1);
                        player.sendMessage(PrismarinConstants.PREFIX + "§cYou do not have enough money.");
                        return;
                    }
                    user.getSeasonData().setBalance(user.getSeasonData().getBalance() - perk.getBuyPrice());
                    service.getUserProvider().saveAsync(user, false);
                    player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully bought the perk " + perk.getIcon().getItemMeta().getDisplayName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set " + PrismarinConstants.PERMISSION_PREFIX + "perks." + perk.name().toLowerCase());
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1);
                    reopen();
                }
            });

            slot++;
        }


        build();
    }

    private void reopen() {
        PerkFrame frame = new PerkFrame(service, player);
        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.4f);
    }
}
