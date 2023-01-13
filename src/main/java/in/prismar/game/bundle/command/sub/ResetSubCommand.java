package in.prismar.game.bundle.command.sub;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserCacheProvider;
import in.prismar.api.user.UserProvider;
import in.prismar.game.bundle.BundleFacade;
import in.prismar.game.bundle.model.Bundle;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.PlayerNotFoundException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.inventory.Frame;
import in.prismar.library.spigot.inventory.button.FrameButton;
import in.prismar.library.spigot.inventory.event.FrameCloseEvent;
import in.prismar.library.spigot.item.container.ItemsContainer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class ResetSubCommand extends HelpSubCommand<Player> {

    private final BundleFacade facade;

    public ResetSubCommand(BundleFacade facade) {
        super("reset");
        this.facade = facade;
        setDescription("Reset a bundle for player");
        setUsage("<player> <id>");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(2);
            if(!facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis bundle does exists");
                return true;
            }
            final String name = arguments.getString(1);
            UserCacheProvider cacheProvider = PrismarinApi.getProvider(UserCacheProvider.class);
            UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
            if(!cacheProvider.existsByName(name)) {
                throw new PlayerNotFoundException(name);
            }
            User user = provider.getUserByUUID(cacheProvider.getUUIDByName(name));
            Bundle bundle = facade.getRepository().findById(id);
            if(user.getSeasonData().getAttachments().containsKey("bundles." + bundle.getId())) {
                user.getSeasonData().getAttachments().remove("bundles." + bundle.getId());
            }
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have reset the bundle §3" + bundle.getId() + " §7for the player §b" + user.getData().getName());
            return true;
        }
        return false;
    }
}
