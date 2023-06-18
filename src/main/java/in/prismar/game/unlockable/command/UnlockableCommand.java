package in.prismar.game.unlockable.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.unlockable.UnlockableService;
import in.prismar.game.unlockable.command.sub.*;
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
public class UnlockableCommand extends HelpCommand<Player> {

    @Inject
    private UnlockableService service;

    public UnlockableCommand() {
        super("unlockable", "Unlockable");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "unlockable");
        setSenders(Player.class);
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(service));
        addChild(new DeleteSubCommand(service));
        addChild(new ListSubCommand(service));
        addChild(new TeleportSubCommand(service));
        addChild(new ResetSubCommand(service));
    }
}
