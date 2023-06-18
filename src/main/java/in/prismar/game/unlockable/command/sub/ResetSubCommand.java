package in.prismar.game.unlockable.command.sub;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserCacheProvider;
import in.prismar.game.unlockable.Unlockable;
import in.prismar.game.unlockable.UnlockableService;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ResetSubCommand extends HelpSubCommand<Player> {

    @Inject
    private UnlockableService service;


    public ResetSubCommand(UnlockableService service) {
        super("reset");
        setUsage("<player> <[id]>");
        setDescription("Reset a unlockable");

        this.service = service;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (arguments.getLength() >= 2) {
            final String name = arguments.getString(1);
            UserCacheProvider userCacheProvider = PrismarinApi.getProvider(UserCacheProvider.class);
            if(!userCacheProvider.existsByName(name)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis player does not exists");
                return true;
            }
            User target = service.getUserProvider().getUserByUUID(userCacheProvider.getUUIDByName(name));

            if(arguments.getLength() >= 3) {
                final String id = arguments.getString(2);
                if (!service.existsUnlockable(id)) {
                    player.sendMessage(PrismarinConstants.PREFIX + "§cThis unlockable does not exists");
                    return true;
                }
                Unlockable unlockable = service.getUnlockable(id);
                target.getSeasonData().getAttachments().remove("unlock." + unlockable.getId());
                service.getUserProvider().saveAsync(target, true);
            } else {
                Set<String> remove = new HashSet<>();
                for(String attachment : target.getSeasonData().getAttachments().keySet()) {
                    if(attachment.startsWith("unlock.")) {
                        remove.add(attachment);
                    }
                }
                for(String attachment : remove) {
                    target.getSeasonData().getAttachments().remove(attachment);
                }

                service.getUserProvider().saveAsync(target, true);
            }
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have reseted §a" + player.getName());
            return true;
        }
        return false;
    }
}
