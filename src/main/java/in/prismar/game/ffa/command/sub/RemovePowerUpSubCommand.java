package in.prismar.game.ffa.command.sub;

import in.prismar.api.PrismarinConstants;
import in.prismar.game.ffa.GameMapFacade;
import in.prismar.game.ffa.model.GameMap;
import in.prismar.game.ffa.model.GameMapPowerUp;
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
public class RemovePowerUpSubCommand extends HelpSubCommand<Player> {

    private final GameMapFacade facade;
    private final GameMapRepository repository;

    public RemovePowerUpSubCommand(GameMapFacade facade) {
        super("removepowerup");
        setPermission(PrismarinConstants.PERMISSION_PREFIX + "map.admin");
        setDescription("Remove latest powerup");
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
            GameMap map = repository.findById(id);
            if(map.getPowerUps().isEmpty()) {
                player.sendMessage(PrismarinConstants.PREFIX + "§cNothing to remove");
                return true;
            }
            GameMapPowerUp powerUp = map.getPowerUps().remove(map.getPowerUps().size() - 1);
            if (powerUp.getHologram() != null) {
                powerUp.getHologram().disable();
            }
            repository.save(map);
            player.sendMessage(PrismarinConstants.PREFIX + "§7You have removed the latest powerup of the map §3" + id);
            return true;
        }
        return false;
    }
}
