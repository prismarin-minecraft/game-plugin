package in.prismar.game.command;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.node.ConfigNode;
import in.prismar.api.configuration.node.ConfigNodeProvider;
import in.prismar.game.Game;
import in.prismar.game.item.impl.drone.Drone;
import in.prismar.game.keycode.KeyCode;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.hologram.Hologram;
import in.prismar.library.spigot.hologram.line.HologramLineType;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        final KeyCode code = new KeyCode((player1, code1) -> {
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
