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
public class EditSubCommand extends HelpSubCommand<Player> {

    private final LeaderboardFacade facade;


    public EditSubCommand(LeaderboardFacade facade) {
        super("edit");
        setDescription("Edit a leaderboard");
        setUsage("<id> <field> <value>");

        this.facade = facade;
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 4) {
            final String id = arguments.getString(1);
            if(!facade.getRepository().existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis leaderboard does not exists");
                return true;
            }
            final String field = arguments.getString(2);
            final String value = arguments.getCombinedArgsFrom(3).replace("&", "§");
            Leaderboard leaderboard = facade.getRepository().findById(id);
            if(field.equalsIgnoreCase("title")) {
                leaderboard.setTitle(value);
            } else if(field.equalsIgnoreCase("format")) {
                leaderboard.setFormat(value);
            } else {
                player.sendMessage(PrismarinConstants.PREFIX + "§cWrong field. Available: title, format");
                return true;
            }
            facade.getRepository().save(leaderboard);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You edited the leaderboard §3" + id);
            return true;
        }
        return false;
    }
}
