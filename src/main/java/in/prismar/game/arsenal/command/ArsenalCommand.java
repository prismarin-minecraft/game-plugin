package in.prismar.game.arsenal.command;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserCacheProvider;
import in.prismar.api.user.UserProvider;
import in.prismar.game.arsenal.ArsenalService;
import in.prismar.game.arsenal.frame.ArsenalFrame;
import in.prismar.library.meta.anno.Inject;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.exception.impl.PlayerNotFoundException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.SpigotCommand;
import in.prismar.library.spigot.meta.anno.AutoCommand;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
@AutoCommand
public class ArsenalCommand extends SpigotCommand<Player> {

    @Inject
    private ArsenalService service;

    private final UserProvider<User> userProvider;
    private final UserCacheProvider userCacheProvider;


    public ArsenalCommand() {
        super("arsenal");
        setAliases("loadout", "lo");
        setSenders(Player.class);

        this.userProvider = PrismarinApi.getProvider(UserProvider.class);
        this.userCacheProvider = PrismarinApi.getProvider(UserCacheProvider.class);
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        User user;
        if(arguments.getLength() >= 1 && player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "loadout.others")){
            final String name = arguments.getString(0);
            if(!userCacheProvider.existsByName(name)) {
                throw new PlayerNotFoundException(name);
            }
            UUID uuid = userCacheProvider.getUUIDByName(name);
            user = userProvider.getUserByUUID(uuid);
        } else {
            user = service.manage(player);
        }
        ArsenalFrame frame = new ArsenalFrame(player, user, service);
        frame.openInventory(player, Sound.ITEM_ARMOR_EQUIP_LEATHER, 0.7f);
        return true;
    }


}
