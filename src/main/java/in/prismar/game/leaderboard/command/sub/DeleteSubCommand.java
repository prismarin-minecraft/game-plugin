package in.prismar.game.leaderboard.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.leaderboard.LeaderboardFacade;
import in.prismar.game.leaderboard.model.Leaderboard;
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

    private final LeaderboardFacade facade;


    public DeleteSubCommand(LeaderboardFacade facade) {
        super("delete");
        setDescription("Delete a leaderboard");
        setUsage("<id>");

        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis leaderboard does not exists");
                return true;
            }
            Leaderboard leaderboard = facade.getRepository().deleteById(id);
            facade.despawn(leaderboard);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You deleted the leaderboard §3" + id);
            return true;
        }
        return false;
    }
}
