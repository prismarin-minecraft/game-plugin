package in.prismar.game.party.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.party.Party;
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
public class KickSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public KickSubCommand(PartyRegistry registry) {
        super("kick");
        setUsage("<player>");
        setDescription("Kick a player of the party");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            if(!registry.hasPartyAndIsOwner(player)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not the owner of a party.");
                return true;
            }
            Party party = registry.getPartyByPlayer(player);
            Player target = arguments.getOnlinePlayer(1);
            if(!registry.kick(party, target)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis player is not in your party.");
                return true;
            }
            target.sendMessage(PrismarinConstants.PREFIX + "§7You were kicked out of the §3party");
            registry.sendMessage(party, PrismarinConstants.PREFIX + "§3" + target.getName() + " §7got kicked out of the party");
            return true;
        }

        return false;
    }
}
