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
public class AddAirdropSubCommand extends HelpSubCommand<Player> {

    private final WarzoneService service;

    public AddAirdropSubCommand(WarzoneService service) {
        super("addairdrop");
        this.service = service;
        setAliases("aa");
        setDescription("Set a airdrop location");

        setPermission(PrismarinConstants.PERMISSION_PREFIX + "warzone.admin");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        service.getConfig().getEntity().getAirdrops().add(player.getLocation());
        service.getConfig().save();
        player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully added a new §cairdrop location");
        return true;
    }
}
