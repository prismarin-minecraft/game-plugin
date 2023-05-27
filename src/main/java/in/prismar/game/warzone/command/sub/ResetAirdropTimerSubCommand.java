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
public class ResetAirdropTimerSubCommand extends HelpSubCommand<Player> {

    private final WarzoneService service;

    public ResetAirdropTimerSubCommand(WarzoneService service) {
        super("resetairdroptimer");
        this.service = service;
        setAliases("rat");
        setDescription("Reset airdrop timer");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "warzone.admin");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        service.getAirdropTask().resetNextAirdropSpawn();
        player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully reset the §cairdrop timer");
        return true;
    }
}
