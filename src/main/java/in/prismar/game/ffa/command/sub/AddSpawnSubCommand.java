package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.ffa.model.FFAMap;
import in.prismar.game.ffa.repository.FFAMapRepository;
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
public class AddSpawnSubCommand extends HelpSubCommand<Player> {

    private final FFAFacade facade;
    private final FFAMapRepository repository;

    public AddSpawnSubCommand(FFAFacade facade) {
        super("addspawn");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "map.admin");
        setDescription("Add a spawn location");
        setUsage("<id>");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 2) {
            final String id = arguments.getString(1);
            if(!repository.existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis map does not exists");
                return true;
            }
            FFAMap map = repository.findById(id);
            map.getSpawns().add(player.getLocation());
            repository.save(map);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have added a spawn to the map §b" + id);
            return true;
        }
        return false;
    }
}
