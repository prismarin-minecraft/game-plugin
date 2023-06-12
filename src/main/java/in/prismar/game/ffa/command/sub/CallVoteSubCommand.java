package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinApi;
import in.prismar.api.PrismarinConstants;
import in.prismar.api.configuration.ConfigStore;
import in.prismar.api.user.User;
import in.prismar.api.user.UserProvider;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.ffa.repository.FFAMapRepository;
import in.prismar.library.common.time.TimeUtil;
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
public class CallVoteSubCommand extends HelpSubCommand<Player> {

    private final FFAFacade facade;
    private final FFAMapRepository repository;

    public CallVoteSubCommand(FFAFacade facade) {
        super("callvote");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "map.call.vote");
        setAliases("callvote");
        setDescription("Call a vote");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(facade.getRotator().isVoteRunning()) {
            player.sendMessage(PrismarinConstants.PREFIX + "§cA vote is already running.");
            return true;
        }
        if(!player.hasPermission(PrismarinConstants.PERMISSION_PREFIX + "map.call.vote.bypass")) {
            UserProvider<User> provider = PrismarinApi.getProvider(UserProvider.class);
            User user = provider.getUserByUUID(player.getUniqueId());
            if(!user.isTimestampAvailable("ffa.callvote")) {
                long difference = user.getTimestamp("ffa.callvote") - System.currentTimeMillis();
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou must wait " + TimeUtil.convertToThreeDigits(difference / 1000) + " before you can call a vote.");
                return true;
            }
            ConfigStore store = PrismarinApi.getProvider(ConfigStore.class);
            int seconds = Integer.valueOf(store.getProperty("ffa.callvote.timer"));
            user.setTimestamp("ffa.callvote", System.currentTimeMillis() + 1000L * seconds);
        }
        facade.getRotator().callVote();
        return true;
    }
}
