package in.prismar.game.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.Game;
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


    private Hologram hologram;

    public TestGameCommand(Game game) {
        super("testgame");
        setAliases("tg");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "testgame");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        return true;
    }


}
