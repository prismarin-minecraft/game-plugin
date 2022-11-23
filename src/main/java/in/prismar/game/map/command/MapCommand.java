package in.prismar.game.map.command;

import in.prismar.game.map.GameMapFacade;
import in.prismar.game.map.command.sub.*;
import in.prismar.library.meta.anno.SafeInitialize;
import in.prismar.library.spigot.command.spigot.template.help.HelpCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class MapCommand extends HelpCommand<Player> {

    @Inject
    private GameMapFacade facade;

    public MapCommand() {
        super("map", "Map");
        setSenders(Player.class);

        setBaseColor("§b");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(facade));
        addChild(new DeleteSubCommand(facade));
        addChild(new ListSubCommand(facade));
        addChild(new AddSpawnSubCommand(facade));
        addChild(new RemoveSpawnSubCommand(facade));
        addChild(new AddPowerUpSubCommand(facade));
        addChild(new RemovePowerUpSubCommand(facade));
        addChild(new PowerUpsSubCommand(facade));
        addChild(new VoteSubCommand(facade));
        addChild(new CallVoteSubCommand(facade));
        addChild(new JoinSubCommand(facade));
        addChild(new LeaveSubCommand(facade));
    }




}
