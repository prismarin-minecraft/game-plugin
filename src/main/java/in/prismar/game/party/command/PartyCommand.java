package in.prismar.game.party.command;

import in.prismar.game.party.PartyRegistry;
import in.prismar.game.party.command.sub.*;
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
public class PartyCommand extends HelpCommand<Player> {

    @Inject
    private PartyRegistry registry;

    public PartyCommand() {
        super("party", "Party");
        setSenders(Player.class);
        setAliases("p");
        setBaseColor("§5");
    }

    @SafeInitialize
    private void initialize() {
        addChild(new CreateSubCommand(registry));
        addChild(new InfoSubCommand(registry));
        addChild(new LeaveSubCommand(registry));
        addChild(new InviteSubCommand(registry));
        addChild(new AcceptSubCommand(registry));
        addChild(new DenySubCommand(registry));
        addChild(new KickSubCommand(registry));
        addChild(new ChatSubCommand(registry));
        addChild(new ForceDisbandSubCommand(registry));
    }
}
