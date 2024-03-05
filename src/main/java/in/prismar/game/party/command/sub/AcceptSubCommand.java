package in.prismar.game.party.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.party.Party;
import in.prismar.game.party.PartyRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.text.InteractiveTextBuilder;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AcceptSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public AcceptSubCommand(PartyRegistry registry) {
        super("accept");
        setUsage("<player>");
        setDescription("Accept invite of a party");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            if(registry.hasParty(player)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou are already in a party.");
                return true;
            }
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
            if(registry.isPartyOverLimitOrEquals(party)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cMax party members reached.");
                registry.removeInvite(party, player);
                return true;
            }
            registry.removeInvite(party, player);
            party.getMembers().add(player);
            registry.getMissionWrapper().getMissionProvider().addProgress(player, "create-or-join-party", 1, 1);
            registry.glow(party, player);
            registry.sendMessage(party, PrismarinConstants.PREFIX + "§b" + player.getName() + " §7joined the party");
            return true;
        }

        return false;
    }
}
