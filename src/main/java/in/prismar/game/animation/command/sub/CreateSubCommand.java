package in.prismar.game.animation.command.sub;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.animation.AnimationFacade;
import in.prismar.game.animation.model.Animation;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final AnimationFacade facade;

    public CreateSubCommand(AnimationFacade facade) {
        super("create");
        this.facade = facade;
        setUsage("<id> <ticks>");
        setDescription("Create animation");
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            final int ticks = arguments.getInteger(2);

            if(facade.getRepository().existsById(id.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis animation already exists");
                return true;
            }
            UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
            User user = provider.getUserByUUID(player.getUniqueId());
            if(!user.containsTag(AnimationFacade.LOCATION_B_KEY) || !user.containsTag(AnimationFacade.LOCATION_A_KEY)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cPlease mark the A and B position. /animation wand");
                return true;
            }
            Location locationA = user.getTag(AnimationFacade.LOCATION_A_KEY);
            Location locationB = user.getTag(AnimationFacade.LOCATION_B_KEY);
            Animation animation = facade.getService().create(id, ticks, locationA, locationB);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully created §a" + animation.getId());
            return true;
        }
        return false;
    }
}
