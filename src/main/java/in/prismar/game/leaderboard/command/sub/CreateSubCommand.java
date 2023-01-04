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
public class CreateSubCommand extends HelpSubCommand<Player> {

    private final LeaderboardFacade facade;


    public CreateSubCommand(LeaderboardFacade facade) {
        super("create");
        setDescription("Create a new leaderboard");
        setUsage("<id> <sorted> <size> <title> <format>");

        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if (arguments.getLength() >= 6) {
            final String id = arguments.getString(1);
            if (facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis leaderboard already exists");
                return true;
            }
            final String sorted = arguments.getString(2);
            final int size = arguments.getInteger(3);
            final String title = arguments.getString(4).replace("&", "§");
            final String format = arguments.getCombinedArgsFrom(5).replace("&", "§");

            Leaderboard leaderboard = facade.getRepository().create(id, player.getLocation(), sorted, size, title, format);
            facade.update(leaderboard);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You successfully created the leaderboard §b" + id);
            return true;
        }
        return false;
    }
}
