package in.prismar.game.hardpoint.command;

import in.prismar.game.hardpoint.HardpointFacade;
import in.prismar.game.hardpoint.command.sub.*;
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
public class HardpointCommand extends HelpCommand<Player> {

    @Inject
    private HardpointFacade facade;

    public HardpointCommand() {
        super("hardpoint", "Hardpoint");
        setAliases("hp");
        setSenders(Player.class);
        setBaseColor("§6");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new JoinSubCommand(facade));
        addChild(new LeaveSubCommand(facade));
        addChild(new AddSpawnSubCommand(facade));
        addChild(new RemoveSpawnSubCommand(facade));
        addChild(new AddPointSubCommand(facade));
        addChild(new RemovePointSubCommand(facade));
        addChild(new OpenSubCommand(facade));
        addChild(new CloseSubCommand(facade));
    }

}
