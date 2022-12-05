package in.prismar.game.party.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.party.PartyRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public CreateSubCommand(PartyRegistry registry) {
        super("create");
        setDescription("Create a party");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(registry.hasParty(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already in a party.");
            return true;
        }
        registry.create(player);
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have created a §bparty");
        return true;
    }
}
