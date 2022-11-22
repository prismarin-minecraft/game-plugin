package in.prismar.essentials.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.essentials.Essentials;
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
public class EssentialsCommand extends SpigotCommand<Player> {

    private final Essentials essentials;

    public EssentialsCommand(Essentials essentials) {
        super("essentials");
        this.essentials = essentials;
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "essentials");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        String type = arguments.getString(0);
        if(type.equalsIgnoreCase("death")) {
            player.sendTitle("§4☠", "", 5, 20, 5);
        } else if(type.equalsIgnoreCase("death2")) {
            player.sendTitle("§4☠ ☠", "", 5, 20, 5);
        } else if(type.equalsIgnoreCase("death3")) {
            player.sendTitle("§4☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠ ☠", "", 5, 20, 5);
        }
        return true;
    }


}
