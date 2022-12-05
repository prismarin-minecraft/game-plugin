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
public class InfoSubCommand extends HelpSubCommand<Player> {

    private final PartyRegistry registry;

    public InfoSubCommand(PartyRegistry registry) {
        super("info");
        setDescription("Info about your party");
        this.registry = registry;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(!registry.hasParty(player)) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cYou are not in a party.");
            return true;
        }
        Party party = registry.getPartyByPlayer(player);
        final String arrow = PrismarinConstants.ARROW_RIGHT;
        player.sendMessage("§8╔═══════════════════════╗");
        player.sendMessage(" ");
        player.sendMessage(arrow + "§7Owner§8: §5" + party.getOwner().getName());
        player.sendMessage(arrow + "§7Members§8: §5" );
        for(Player member : party.getMembers()) {
            player.sendMessage(arrow + "  §§- §5" + member.getName());
        }
        player.sendMessage(" ");
        player.sendMessage("§8╚═══════════════════════╝");

        return true;
    }
}
