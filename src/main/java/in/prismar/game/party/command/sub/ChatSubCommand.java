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
public class ChatSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public ChatSubCommand(PartyRegistry registry) {
        super("chat");
        setAliases("c");
        setUsage("<[message]>");
        setDescription("Send message to all party members");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 1) {
            if(!registry.hasParty(player)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not in a party.");
                return true;
            }
            Party party = registry.getPartyByPlayer(player);
            if(arguments.getLength() >= 2) {
                String message = arguments.getCombinedArgsFrom(1);
                registry.sendMessage(party, PartyRegistry.PARTY_CHAT_PREFIX + player.getName() + " " + PrismarinConstants.ARROW_RIGHT +" §d" + message);
                return true;
            }
            if(registry.togglePartyChat(player)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§7You have enabled §dparty chat");
            } else {
                player.sendMessage(PrismarinConstants.PREFIX + "§7You have disabled §dparty chat");
            }
            return true;
        }
        return false;
    }
}
