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
public class LeaveSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public LeaveSubCommand(PartyRegistry registry) {
        super("leave");
        setDescription("Leave your party");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (!registry.hasParty(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not in a party.");
            return true;
        }
        Party party = registry.getPartyByPlayer(player);
        boolean wasOwner = party.getOwner().getUniqueId().equals(player.getUniqueId());
        boolean disband = registry.leave(party, player);
        if (disband) {
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have disbanded your §3party");
        } else {
            registry.sendMessage(party, PrismarinConstants.PREFIX + "§3" + player.getName() + " §7left the party");
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have left your §3party");
            if (wasOwner) {
                registry.sendMessage(party, PrismarinConstants.PREFIX + "§b" + party.getOwner().getName() + " §7is the new owner of the party");
            }
        }
        return true;
    }
}
