package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.ai.ConvoyEscort;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class TestGameCommand extends SpigotCommand<Player> {

    @Inject
    private Game game;

    public TestGameCommand(Game game) {
        super("testgame");
        setAliases("tg");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "testgame");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (!player.getName().equals("ReaperMaga")) {
            player.sendMessage("Only ReaperMaga can execute this command!");
            return true;
        }
        ConvoyEscort escort = new ConvoyEscort(game, player.getLocation());
        player.sendMessage("SPAWNED!");
        return true;
    }


}
