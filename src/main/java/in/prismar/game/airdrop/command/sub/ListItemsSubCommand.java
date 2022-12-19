package in.prismar.game.airdrop.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.airdrop.AirDrop;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.airdrop.loot.AirDropItem;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.inventory.template.Pager;
import in.prismar.library.spigot.item.ItemBuilder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ListItemsSubCommand extends HelpSubCommand<Player> {

    private final AirDropRegistry registry;

    public ListItemsSubCommand(AirDropRegistry registry) {
        super("listitems");
        setAliases("listitem");
        setDescription("List all items");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        openPager(player);
        return true;
    }

    private Pager openPager(Player player) {
        Pager pager = new Pager("§cAirdrop Items", 6);
        pager.fill();
        for(AirDropItem item : registry.getLootTable().getEntity()) {
            pager.addItem(new ItemBuilder(item.getItem().getItem())
                    .addLore("§c")
                    .addLore(" §8" + PrismarinConstants.DOT + " §7Chance§8: §b" + item.getChance() )
                    .addLore(" §8" + PrismarinConstants.DOT + " §3Click §7to remove item")
                    .addLore("§c")
                    .build(), (ClickFrameButtonEvent) (player1, event) -> {
                registry.getLootTable().getEntity().remove(item);
                registry.getLootTable().save();
                openPager(player);
            });
        }
        pager.build();
        pager.openInventory(player, Sound.UI_BUTTON_CLICK, 0.65f);
        return pager;
    }
}
