package in.prismar.game.party.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.party.Party;
import in.prismar.game.party.PartyRegistry;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ForceDisbandSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public ForceDisbandSubCommand(PartyRegistry registry) {
        super("forcedisband");
        setUsage("<owner>");
        setDescription("Force disband a party");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "party.forcedisband");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() < 2) {
            return false;
        }
        Player target = arguments.getOnlinePlayer(1);
        if (!registry.hasParty(target)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cThis player is not in a party.");
            return true;
        }
        Party party = registry.getPartyByPlayer(target);
        registry.sendMessage(party, PrismarinConstants.PREFIX + "§cThe party has been force disbanded by §e" + player.getName());
        List<Player> members = new ArrayList<>(party.getMembers());
        for(Player member : members) {
            registry.leave(party, member);
        }
        registry.leave(party, party.getOwner());
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have force disbanded the party §c" + target.getName());
        return true;
    }
}
