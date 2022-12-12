package in.prismar.game.bundle.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.command.sub.*;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.meta.anno.SafeInitialize;
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
public class BundleCommand extends HelpCommand<Player> {

    @Inject
    private BundleFacade facade;

    public BundleCommand() {
        super("bundle", "Bundle");
        setAliases("bundles");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "bundle.admin");
        setSenders(Player.class);
        setBaseColor("§a");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(facade));
        addChild(new DeleteSubCommand(facade));
        addChild(new ListSubCommand(facade));
        addChild(new EditSubCommand(facade));
        addChild(new GetSubCommand(facade));
    }
}
