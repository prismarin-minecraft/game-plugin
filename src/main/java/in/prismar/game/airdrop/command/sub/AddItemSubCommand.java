package in.prismar.game.airdrop.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.airdrop.loot.AirDropItem;
import in.prismar.library.common.math.MathUtil;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.item.ItemUtil;
import in.prismar.library.spigot.item.container.ItemContainer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AddItemSubCommand extends HelpSubCommand<Player> {

    private final AirDropRegistry registry;

    public AddItemSubCommand(AirDropRegistry registry) {
        super("additem");
        setUsage("<chance>");
        setDescription("Add a item to the loot table");

        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2){
            double chance = MathUtil.clamp(arguments.getDouble(1), 0, 1);
            if(!ItemUtil.hasItemInHand(player, true)){
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease hold something in your hand");
                return true;
            }
            final ItemStack stack = player.getInventory().getItemInMainHand().clone();

            registry.getLootTable().getEntity().add(new AirDropItem(new ItemContainer(stack), chance));
            registry.getLootTable().save();
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have sucessfully added a §bitem §7to the loot table.");
            return true;
        }
        return false;
    }
}
