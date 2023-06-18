package in.prismar.game.unlockable.command.sub;

import com.google.common.base.Joiner;
import in.prismar.api.PrismarinConstants;
import in.prismar.game.unlockable.Unlockable;
import in.prismar.game.unlockable.UnlockableService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ListSubCommand extends HelpSubCommand<Player> {

    @Inject
    private UnlockableService service;


    public ListSubCommand(UnlockableService service) {
        super("list");
        setDescription("List all unlockables");

        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7Unlockables§8: §a" + Joiner.on("§8, §a").join(service.getFile().getEntity().values()));
        return true;
    }
}
