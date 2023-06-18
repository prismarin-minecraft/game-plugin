package in.prismar.game.unlockable.command.sub;

import in.prismar.api.PrismarinConstants;
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
public class CreateSubCommand extends HelpSubCommand<Player> {

    @Inject
    private UnlockableService service;


    public CreateSubCommand(UnlockableService service) {
        super("create");
        setUsage("<id> <display>");
        setDescription("Create a new unlockable");

        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if (service.existsUnlockable(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis id is already taken");
                return true;
            }
            final String display = arguments.getCombinedArgsFrom(2).replace("&", "§");
            service.create(id, display, player.getLocation().clone());
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully created §a" + id);
            return true;
        }
        return false;
    }
}
