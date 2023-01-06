package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.ffa.model.GameMap;
import in.prismar.game.ffa.repository.GameMapRepository;
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
public class ListSubCommand extends HelpSubCommand<Player> {

    private final GameMapFacade facade;
    private final GameMapRepository repository;

    public ListSubCommand(GameMapFacade facade) {
        super("list");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "map.admin");
        setDescription("List all maps");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        player.sendMessage(PrismarinConstants.PREFIX + "§7Maps§8: ");
        for(GameMap map : repository.findAll()) {
            player.sendMessage(PrismarinConstants.PREFIX + "  §8- §b" + map.getId() +
                    "§8(§7Spawns§8: §3" + map.getSpawns().size()+"§8, §7PowerUps§8: §3"+map.getPowerUps().size()+"§8)");
        }
        return true;
    }
}