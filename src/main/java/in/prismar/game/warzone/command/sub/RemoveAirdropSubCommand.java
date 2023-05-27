package in.prismar.game.warzone.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.warzone.WarzoneService;
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
public class RemoveAirdropSubCommand extends HelpSubCommand<Player> {

    private final WarzoneService service;

    public RemoveAirdropSubCommand(WarzoneService service) {
        super("removeairdrop");
        this.service = service;
        setAliases("ra");
        setDescription("Remove latest airdrop location");

        setPermission(PrismarinConstants.PERMISSION_PREFIX + "warzone.admin");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(service.getConfig().getEntity().getAirdrops().isEmpty()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cThere is nothing to remove.");
            return true;
        }
        service.getConfig().getEntity().getAirdrops().remove(service.getConfig().getEntity().getAirdrops().size()-1);
        service.getConfig().save();
        player.sendMessage(PrismarinConstants.PREFIX + "§7You have removed the latest §cairdrop location");
        return true;
    }
}
