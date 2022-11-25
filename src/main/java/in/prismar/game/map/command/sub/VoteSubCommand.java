package in.prismar.game.map.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.map.GameMapFacade;
import in.prismar.game.map.model.GameMap;
import in.prismar.game.map.repository.GameMapRepository;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class VoteSubCommand extends HelpSubCommand<Player> {

    private final GameMapFacade facade;
    private final GameMapRepository repository;

    public VoteSubCommand(GameMapFacade facade) {
        super("vote");
        setAliases("v");
        setDescription("Vote for a map");
        setUsage("<id>");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!repository.existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis map does not exist");
                return true;
            }
            GameMap map = repository.findById(id);
            if(!facade.getRotator().canVoteFor(map)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cNo vote is currently running");
                return true;
            }
            if(facade.getRotator().getVoting().get(map.getId()).contains(player.getUniqueId())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cYou already voted");
                return true;
            }
            facade.getRotator().voteForMap(map, player.getUniqueId());
            Bukkit.broadcastMessage(PrismarinConstants.PREFIX + "§b" + player.getName() + " §7has voted for §3" + map.getFancyName());

            player.sendMessage(PrismarinConstants.PREFIX + "§7You have voted for the map §b" + map.getFancyName());
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.65f, 1);
            return true;
        }
        return false;
    }
}
