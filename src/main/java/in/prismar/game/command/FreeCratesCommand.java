package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.event.ClickFrameButtonEvent;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class FreeCratesCommand extends SpigotCommand<Player> {


    public FreeCratesCommand(Game game) {
        super("free");
        setAliases("freecrates", "freecrate");
        setSenders(Player.class);
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        Frame frame = new Frame("§d§lFREE CRATES", 3);
        frame.fill();
        frame.addButton(11, new ItemBuilder(Material.BOOK).setName("§a§lVote")
                .addLore("§c")
                        .addLore(PrismarinConstants.ARROW_RIGHT + " §7You will receive §a1x Common crate §7per vote")
                        .addLore("§c")
                        .addLore("§7Click me to vote")
                .build(), (ClickFrameButtonEvent) (player1, event) -> {
                    player1.closeInventory();
                    player1.performCommand("vote");
                });
        frame.addButton(15, new ItemBuilder(Material.PAPER).setName("§9§lDiscord")
                .addLore("§c")
                .addLore(PrismarinConstants.ARROW_RIGHT + " §7You will receive §62x Rare crates")
                .addLore("§c")
                .addLore("§7Click me to link yourself")
                .build(), (ClickFrameButtonEvent) (player1, event) -> {
            player1.closeInventory();
            player1.performCommand("link");
        });
        frame.build();
        frame.openInventory(player, Sound.UI_BUTTON_CLICK, 0.5f);
        return true;
    }


}
