package in.prismar.game.warzone.command;

import in.prismar.game.warzone.WarzoneService;
import in.prismar.game.warzone.command.sub.AddAirdropSubCommand;
import in.prismar.game.warzone.command.sub.RemoveAirdropSubCommand;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
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
public class WarzoneCommand extends HelpCommand<Player> {


    @Inject
    private WarzoneService service;

    public WarzoneCommand() {
        super("warzone", "Warzone");
        setAliases("wz");
        setBaseColor("§c");
        setSenders(Player.class);
    }

    @SafeInitialize
    private void initialize() {
        addChild(new AddAirdropSubCommand(service));
        addChild(new RemoveAirdropSubCommand(service));
    }

    @Override
    public boolean raw(Player player, SpigotArguments arguments) {
        if(arguments.getLength() == 0) {
            player.performCommand("warp warzone");
            return false;
        }
        return true;
    }

}
