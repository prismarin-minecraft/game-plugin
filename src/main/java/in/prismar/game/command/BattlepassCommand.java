package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.item.ItemBuilder;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class BattlepassCommand extends SpigotCommand<Player> {

    public BattlepassCommand(Game game) {
        super("battlepass");
        setSenders(Player.class);
        setAliases("bp");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        Frame frame = new Frame("§f七七七七七七七七ㇺ", 5);
        frame.addButton(10, new ItemBuilder(Material.WOODEN_PICKAXE).setCustomModelData(2).setName("§cM4A1").build());

        frame.addButton(19, new ItemBuilder(Material.MAP).setCustomModelData(1).setName(" ").build());
        frame.addButton(29, new ItemBuilder(Material.CHEST).setName("§6Crate").build());

        frame.addButton(20, new ItemBuilder(Material.MAP).setCustomModelData(2).setName(" ").build());
        frame.addButton(21, new ItemBuilder(Material.MAP).setCustomModelData(3).setName(" ").build());
        frame.addButton(22, new ItemBuilder(Material.MAP).setCustomModelData(2).setName(" ").build());
        frame.addButton(23, new ItemBuilder(Material.MAP).setCustomModelData(3).setName(" ").build());
        frame.addButton(24, new ItemBuilder(Material.MAP).setCustomModelData(2).setName(" ").build());
        frame.addButton(25, new ItemBuilder(Material.MAP).setCustomModelData(4).setName(" ").build());
        frame.build();
        frame.openInventory(player);
        return true;
    }


}
