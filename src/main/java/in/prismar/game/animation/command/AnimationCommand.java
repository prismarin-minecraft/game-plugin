package in.prismar.game.animation.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.animation.AnimationFacade;
import in.prismar.game.animation.command.sub.*;
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
public class AnimationCommand extends HelpCommand<Player> {

    @Inject
    private AnimationFacade facade;

    public AnimationCommand() {
        super("animation", "Animation");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "animation");
        setSenders(Player.class);
        setAliases("animations");


    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(facade));
        addChild(new DeleteSubCommand(facade));
        addChild(new ListSubCommand(facade));
        addChild(new InfoSubCommand(facade));
        addChild(new TeleportSubCommand(facade));
        addChild(new ChangeTicksSubCommand(facade));
        addChild(new SetStaySubCommand(facade));
        addChild(new WandSubCommand(facade));
        addChild(new AddFrameSubCommand(facade));
        addChild(new RemoveFrameSubCommand(facade));
        addChild(new ChangeToFrameSubCommand(facade));
        addChild(new SaveFrameSubCommand(facade));
        addChild(new PlaySubCommand(facade));
    }

}
