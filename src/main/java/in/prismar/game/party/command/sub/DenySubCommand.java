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
public class DenySubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public DenySubCommand(PartyRegistry registry) {
        super("deny");
        setUsage("<player>");
        setDescription("Deny the invitation of a party");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            Player target = arguments.getOnlinePlayer(1);
            if(!registry.hasParty(target)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis player does not have a party.");
                return true;
            }
            Party party = registry.getPartyByPlayer(target);
            if(!registry.hasInvite(party, player)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou weren't invited to this party.");
                return true;
            }
            registry.removeInvite(party, player);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You declined the party invitation from §3" + party.getOwner().getName());
            party.getOwner().sendMessage(PrismarinConstants.PREFIX + "§3" + player.getName() + " §7declined the party invitation");
            return true;
        }

        return false;
    }
}
