package in.prismar.game.web.config.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.web.config.command.sub.MigrateSubCommand;
import in.prismar.game.web.config.file.ConfigNodeFile;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class ConfigCommand extends HelpCommand<CommandSender> {

    @Inject
    private ConfigNodeFile file;

    public ConfigCommand() {
        super("config", "Config");
        setAliases("cfg");
        setSenders(Player.class, ConsoleCommandSender.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "config");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new MigrateSubCommand(file));
    }
}
