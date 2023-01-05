package in.prismar.game.ffa.arsenal.command;

import in.prismar.game.ffa.arsenal.ArsenalService;
import in.prismar.game.ffa.arsenal.frame.ArsenalFrame;
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
public class ArsenalCommand extends SpigotCommand<Player> {

    @Inject
    private ArsenalService service;


    public ArsenalCommand() {
        super("arsenal");
        setAliases("loadout", "lo");
        setSenders(Player.class);
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        ArsenalFrame frame = new ArsenalFrame(player, service);
        frame.openInventory(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.7f);
        return true;
    }


}
