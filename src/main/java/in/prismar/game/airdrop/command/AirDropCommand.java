package in.prismar.game.airdrop.command;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.airdrop.AirDropRegistry;
import in.prismar.game.airdrop.command.sub.AddItemSubCommand;
import in.prismar.game.airdrop.command.sub.CallSubCommand;
import in.prismar.game.airdrop.command.sub.ListItemsSubCommand;
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
public class AirDropCommand extends HelpCommand<Player> {

    @Inject
    private AirDropRegistry registry;

    public AirDropCommand() {
        super("airdrop", "Airdrop");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "airdrop");
        setSenders(Player.class);
        setBaseColor("§c");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new AddItemSubCommand(registry));
        addChild(new ListItemsSubCommand(registry));
        addChild(new CallSubCommand(registry));
    }
}
