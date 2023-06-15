package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
import in.prismar.game.keycode.KeyCodeFrame;
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
       // setPermission(PrismarinConstants.PERMISSION_PREFIX + "testgame");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        final KeyCodeFrame code = new KeyCodeFrame((player1, code1) -> {
            if(code1.equals("05071")) {
                player.closeInventory();
                player.sendMessage(PrismarinConstants.PREFIX + "§aSheesh");
                return true;
            }
            return false;
        });
        code.openInventory(player);
        return true;
    }


}
