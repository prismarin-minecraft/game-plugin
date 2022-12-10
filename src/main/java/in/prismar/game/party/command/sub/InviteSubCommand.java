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
public class InviteSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public InviteSubCommand(PartyRegistry registry) {
        super("invite");
        setUsage("<player>");
        setDescription("Invite someone to your party");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            if(!registry.hasPartyAndIsOwner(player)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou need to be the owner of a party.");
                return true;
            }
            Player target = arguments.getOnlinePlayer(1);
            Party party = registry.getPartyByPlayer(player);
            if(registry.hasInvite(party, target)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou have already invited this player.");
                return true;
            }
            if(registry.hasParty(target)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis player is already in a party.");
                return true;
            }
            if(party.getMembers().size() >= registry.getMaxPartySize() - 1) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cMax party members reached.");
                return true;
            }
            party.getInvites().add(target);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You invited §b" + target.getName() + " §7to the party.");

            InteractiveTextBuilder textBuilder = new InteractiveTextBuilder(PrismarinConstants.PREFIX);
            textBuilder.addText("§8[§aAccept§8]", "/party accept " + player.getName(), "§7Click me to accept this invite");
            textBuilder.addText(" ");
            textBuilder.addText("§8[§cDeny§8]", "/party deny " + player.getName(), "§7Click me to deny this invite");
            target.sendMessage(PrismarinConstants.PREFIX + "§7You were invited to §b" + player.getName() + "'s §7party");
            target.spigot().sendMessage(textBuilder.build());
            return true;
        }

        return false;
    }
}
