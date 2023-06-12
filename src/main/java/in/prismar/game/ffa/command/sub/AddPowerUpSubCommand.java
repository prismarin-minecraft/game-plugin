package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.FFAFacade;
import in.prismar.game.ffa.model.FFAMap;
import in.prismar.game.ffa.model.FFAMapPowerUp;
import in.prismar.game.ffa.repository.FFAMapRepository;
import in.prismar.library.spigot.command.exception.CommandException;
import in.prismar.library.spigot.command.spigot.SpigotArguments;
import in.prismar.library.spigot.command.spigot.template.help.HelpSubCommand;
import in.prismar.library.spigot.location.LocationUtil;
import org.bukkit.entity.Player;

/**
 * Copyright (c) Maga, All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Maga
 **/
public class AddPowerUpSubCommand extends HelpSubCommand<Player> {

    private final FFAFacade facade;
    private final FFAMapRepository repository;

    public AddPowerUpSubCommand(FFAFacade facade) {
        super("addpowerup");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "map.admin");
        setDescription("Add power up");
        setUsage("<id> <powerup>");
        this.facade = facade;
        this.repository = facade.getRepository();
    }

    @Override
    public boolean send(Player player, SpigotArguments arguments) throws CommandException {
        if(arguments.getLength() >= 3) {
            final String id = arguments.getString(1);
            if(!repository.existsById(id)) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis map does not exists");
                return true;
            }
            final String powerUp = arguments.getString(2);
            if(!facade.getPowerUpRegistry().existsById(powerUp.toLowerCase())) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cThis powerup does not exists. /map powerups");
                return true;
            }
            FFAMap map = repository.findById(id);
            map.getPowerUps().add(new FFAMapPowerUp(powerUp.toLowerCase(), LocationUtil.getCenterOfBlock(player.getLocation())));
            repository.save(map);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have added a powerup to the map §b" + id);
            return true;
        }
        return false;
    }
}
