package in.prismar.game.item.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.item.CustomItem;
import in.prismar.game.item.CustomItemRegistry;
import in.prismar.game.item.command.sub.AmmoSubCommand;
import in.prismar.game.item.command.sub.GetSubCommand;
import in.prismar.game.item.command.sub.ListSubCommand;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class CustomItemCommand extends HelpCommand<Player> {

    @Inject
    private CustomItemRegistry registry;


    public CustomItemCommand() {
        super("customitem", "CustomItem");
        setBaseColor("§6");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "customitem");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new ListSubCommand(registry));
        addChild(new GetSubCommand(registry));
        addChild(new AmmoSubCommand(registry));
    }




}
