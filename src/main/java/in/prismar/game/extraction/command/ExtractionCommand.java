package in.prismar.game.extraction.command;

import in.prismar.game.extraction.ExtractionFacade;
import in.prismar.game.extraction.command.sub.*;
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
public class ExtractionCommand extends HelpCommand<Player> {

    @Inject
    private ExtractionFacade facade;

    public ExtractionCommand() {
        super("extraction", "Extraction");
        setSenders(Player.class);
        setBaseColor("§c");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new JoinSubCommand(facade));
        addChild(new LeaveSubCommand(facade));
        addChild(new AddSpawnSubCommand(facade));
        addChild(new RemoveSpawnSubCommand(facade));
        addChild(new SpawnsSubCommand(facade));
        addChild(new OpenSubCommand(facade));
        addChild(new CloseSubCommand(facade));
    }
}
