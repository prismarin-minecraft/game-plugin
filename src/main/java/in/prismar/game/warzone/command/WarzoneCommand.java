package in.prismar.game.warzone.command;

import in.prismar.game.warzone.WarzoneService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.player.PlayerCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class WarzoneCommand extends PlayerCommand {


    @Inject
    private WarzoneService service;

    public WarzoneCommand() {
        super("warzone");
        setAliases("wz");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.performCommand("warp warzone");
        return true;
    }
}
