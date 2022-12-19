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
public class CallSubCommand extends HelpSubCommand<Player> {

    private final AirDropRegistry registry;

    public CallSubCommand(AirDropRegistry registry) {
        super("call");
        setDescription("Call Airdrop");

        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        registry.callAirDrop(player.getLocation());
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have sucessfully called a §bairdrop§7.");
        return true;
    }
}
