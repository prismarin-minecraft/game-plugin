package in.prismar.game.item.command;

import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.frame.ModificationsFrame;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class ModificationsCommand extends SpigotCommand<Player> {

    @Inject
    private CustomItemRegistry registry;

    public ModificationsCommand() {
        super("modifications");
        setSenders(Player.class);
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        ModificationsFrame frame = new ModificationsFrame(registry);
        frame.openInventory(player, Sound.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, 0.7F);
        return true;
    }
}
