package in.prismar.game.unlockable.command.sub;

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
public class DeleteSubCommand extends HelpSubCommand<Player> {

    @Inject
    private UnlockableService service;


    public DeleteSubCommand(UnlockableService service) {
        super("delete");
        setUsage("<id>");
        setDescription("Delete a unlockable");

        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if (!service.existsUnlockable(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis unlockable does not exists");
                return true;
            }
            Unlockable unlockable = service.getUnlockable(id);
            service.delete(unlockable);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have deleted §e" + id);
            return true;
        }
        return false;
    }
}
