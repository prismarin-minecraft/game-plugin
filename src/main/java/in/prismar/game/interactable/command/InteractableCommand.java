package in.prismar.game.interactable.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.interactable.InteractableService;
import in.prismar.game.interactable.command.sub.*;
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
public class InteractableCommand extends HelpCommand<Player> {

    @Inject
    private InteractableService service;

    public InteractableCommand() {
        super("interactable", "Interactable");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "interactable");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(service));
        addChild(new DeleteSubCommand(service));
        addChild(new ListSubCommand(service));
        addChild(new SetSubCommand(service));
        addChild(new InfoSubCommand(service));
        addChild(new TeleportSubCommand(service));
        addChild(new AddCodeSubCommand(service));
    }
}
