package in.prismar.game.leaderboard.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.leaderboard.LeaderboardFacade;
import in.prismar.game.leaderboard.command.sub.*;
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
public class LeaderboardCommand extends HelpCommand<Player> {

    @Inject
    private LeaderboardFacade facade;

    public LeaderboardCommand() {
        super("leaderboard", "Leaderboard");
        setSenders(Player.class);
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "leaderboard");
        setBaseColor("§c");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(facade));
        addChild(new DeleteSubCommand(facade));
        addChild(new ListSubCommand(facade));
        addChild(new EditSubCommand(facade));
        addChild(new MoveHereSubCommand(facade));
        addChild(new UpdateSubCommand(facade));
    }
}
